package com.ybg.rp.ua.device

import com.ybg.rp.ua.base.GoodsTypeOne
import com.ybg.rp.ua.themeStore.ThemeStoreGoodsInfo
import com.ybg.rp.ua.themeStore.ThemeStoreGoodsTypeOne
import grails.transaction.Transactional
import org.hibernate.Query

@Transactional
class VendLayerTrackGoodsService {

    @Transactional(readOnly = true)
    def getLayerTrackListByMachine(VendMachineInfo machineInfo) {
        VendLayerTrackInfo.findAllByVendMachineAndIsCabinet(machineInfo, 0 as Short, [sort: "layer", order: "asc"])
    }

    @Transactional(readOnly = true)
    def getCabinetListByMachine(VendMachineInfo machineInfo) {
        VendLayerTrackInfo.findAllByVendMachineAndIsCabinet(machineInfo, 1 as Short, [sort: "layer", order: "asc"])
    }

    @Transactional(readOnly = true)
    def listLayerTrackGoodsByTrackInfo(VendLayerTrackInfo trackInfo) {
        def hql = "from VendLayerTrackGoods v where v.layer=? and v.vendMachine=? order by v.orbitalNo asc "
        def data = []
        VendLayerTrackGoods.executeQuery(hql, [trackInfo.layer, trackInfo.vendMachine]).each { trackGoods ->
            def map = [:]
            map.goodsName = trackGoods.goods?.name ?: ""
            map.standardPrice = trackGoods.goods?.realPrice ?: 0
            map.id = trackGoods.goods?.id ?: ""
            map.orbitalNo = trackGoods.orbitalNo
            map.layer = trackGoods.layer
            map.currentInventory = trackGoods.currentInventory
            map.largestInventory = trackGoods.largestInventory
            map.lid = trackGoods.id
            data.add(map)
        }
        data
    }

    def setTrackGoodsByLayerIds(String layerIds, Long goodsId) {
        def goods = ThemeStoreGoodsInfo.get(goodsId)
        if (goods) {
            def ids = layerIds.split(",")
            for (String id: ids) {
                if (id != null && !"".equals(id)) {
                    try {
                        def trackGoods = VendLayerTrackGoods.get(Long.valueOf(id))
                        if (trackGoods) {
                            trackGoods.goods = goods
                            trackGoods.save flush: true
                        }
                    } catch (Exception e) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    def setTrackGoodsNum(String layerIds, Integer num) {
        def ids = layerIds.split(",")
        for (String id: ids) {
            if (id != null && !"".equals(id)) {
                try {
                    def trackGoods = VendLayerTrackGoods.get(Long.valueOf(id))
                    if (trackGoods) {
                        trackGoods.currentInventory = num//设置当前库存数量
                        trackGoods.save flush: true
                    }
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }
    }

    def getEmptyDoorTrasckNos(Long sid) {
        def trackInfo = VendLayerTrackInfo.get(sid)
        def trackGoodsList = VendLayerTrackGoods.findAllByVendMachineAndLayerAndCurrentInventory(trackInfo.vendMachine, trackInfo.layer, 0)
        trackGoodsList*.orbitalNo
    }

    def getGoodsNumByLayerId(Long lid) {
        VendLayerTrackGoods.get(lid)?.currentInventory
    }

    def updateLayerGoods(List actionParams) {
        actionParams.each { actionParam ->
            Long layerId = actionParam.layerId
            Long goodsId = actionParam.goodsId
            Integer goodsNum = actionParam.goodsNum
            def trackGoods = VendLayerTrackGoods.get(layerId)
            if (trackGoods) {
                trackGoods.goods = ThemeStoreGoodsInfo.get(goodsId)
                trackGoods.currentInventory = Math.min(goodsNum, trackGoods.largestInventory)//不能超过最大库存
                trackGoods.save flush: true
            }
        }
    }

    def queryGoodsByTypeOne(Long machineId, Long typeOneId, Integer pageNum, Integer pageSize) {
        def map = [:]
        //准备参数
        def typeOne = GoodsTypeOne.get(typeOneId)
        def machine = VendMachineInfo.get(machineId)
        def goods = ThemeStoreGoodsTypeOne.findAllByTypeOne(typeOne)*.goodsInfo
        //查询
        def c = VendLayerTrackGoods.createCriteria()
        def results = c.list (max: pageSize, offset: (pageNum - 1) * pageSize) {
            projections {
                sum("currentInventory")
                groupProperty("goods")
            }
            and {
                'in'("goods", goods)
                eq("vendMachine", machine)
                eq("sellStatus", 1 as Short)
                eq("workStatus", 1 as Short)
                gt("currentInventory", 0)//过虑空库存商品
            }
        }
        //构造返回数据集
        def list = []
        for (Object object : results.resultList) {
            Object[] obj = (Object[]) object
            def num = Long.valueOf(obj[0])
            def goodsInfo = obj[1]
            def vo = [:]
            vo.gid = goodsInfo.id
            vo.kucun = num
            vo.price = goodsInfo.realPrice
            vo.goodsName = goodsInfo.name
            vo.goodsDesc = goodsInfo.specifications
            vo.goodsPic = goodsInfo.picId
            list.add(vo)
        }
        //构造分页结果
        def totalCount = results.size()
        def pageCount = 0
        if (totalCount % pageSize == 0) {
            pageCount = (int) (totalCount / pageSize)
        } else {
            pageCount = (int) (totalCount / pageSize) + 1
        }
        map.totalPageSize = pageCount
        map.list = list
        map.success = true
        map
    }

    def listGoodsByMachine(VendMachineInfo machineInfo) {
        //查询
        def c = VendLayerTrackGoods.createCriteria()
        def results = c.list {
            projections {
                sum("currentInventory")
                sum("largestInventory")
                groupProperty("goods")
            }
            and {
                eq("vendMachine", machineInfo)
                ltProperty("currentInventory", "largestInventory")
                isNotNull("goods")
            }
        }
        //构造返回数据集
        def list = []
        for (Object object : results) {
            Object[] obj = (Object[]) object
            def num1 = Long.valueOf(obj[0])
            def num2 = Long.valueOf(obj[1])
            def goodsInfo = obj[2]
            def vo = [:]
            vo.gid = goodsInfo?.id
            vo.num1 = num1
            vo.num2 = num2
            vo.goodsName = goodsInfo?.name
            vo.storeName = machineInfo.themeStore.name
            list.add(vo)
        }
        list
    }

    def listLayerByGoods(ThemeStoreGoodsInfo goods) {
        //查询
        def c = VendLayerTrackGoods.createCriteria()
        def results = c.list {
            and {
                eq("goods", goods)
                ltProperty("currentInventory", "largestInventory")
            }
        }
        results
    }
}
