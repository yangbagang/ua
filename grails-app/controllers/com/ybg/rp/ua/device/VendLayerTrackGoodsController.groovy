package com.ybg.rp.ua.device

import com.ybg.rp.ua.themeStore.ThemeStoreBaseInfo
import com.ybg.rp.ua.utils.MsgPushHelper
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON

class VendLayerTrackGoodsController {

    def vendLayerTrackGoodsService

    def partnerUserOperationLogService

    /**
     * 商品管理模块,初始化主机信息。
     * @param themeStoreId
     * @param token
     * @return
     */
    def getGoodsInfo(Long themeStoreId, String token) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            def themeStore = ThemeStoreBaseInfo.get(themeStoreId)
            if (themeStore) {
                def machine = VendMachineInfo.findByThemeStoreAndIsReal(themeStore, 1 as Short)
                if (machine) {
                    def trackList = vendLayerTrackGoodsService.getLayerTrackListByMachine(machine)
                    if(trackList != null && trackList.size() > 0) {
                        def dataList = []
                        def layerNums = []
                        def layerOrbitalNum = []
                        for (VendLayerTrackInfo trackInfo: trackList) {
                            def trackGoodsList = vendLayerTrackGoodsService.listLayerTrackGoodsByTrackInfo(trackInfo)
                            dataList.add(trackGoodsList)
                            layerNums.add(trackInfo.layer)
                            layerOrbitalNum.add(trackInfo.orbitalNum)
                        }

                        def cabinetList = vendLayerTrackGoodsService.getCabinetListByMachine(machine)

                        map.dataList = dataList
                        map.layerNum = trackList.size()
                        map.layerNums = layerNums
                        map.layerOrbitalNum = layerOrbitalNum
                        map.subList = cabinetList

                        map.success = true
                        map.status = "000"
                    } else {
                        map.success = false
                        map.message = "售卖机数据暂未上传"
                        map.status = "001"
                    }
                } else {
                    map.success = false
                    map.message = "售卖机数据暂未上传"
                    map.status = "001"
                }
            } else {
                map.success = false
                map.message = "售卖机数据暂未上传"
                map.status = "001"
            }
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
            map.status = "002"
        }
        render map as JSON
    }

    /**
     * 商品管理模块,获取格子机数据。
     * @param token
     * @param sid 格子机对应层id
     */
    def selectSubCabinetGoods(String token, Long sid) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            def trackInfo = VendLayerTrackInfo.get(sid)
            if (trackInfo) {
                def dataList = vendLayerTrackGoodsService.listLayerTrackGoodsByTrackInfo(trackInfo)
                map.success = true
                map.dataList = dataList
            } else {
                map.success = false
                map.message = "格子机不存在"
            }
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 设置轨道销售商品
     * @param token
     * @param layerIds
     * @param goodsId
     * @return
     */
    def setTrackGoods(String token, String layerIds, Long goodsId) {
        println "layerIds=${layerIds}"
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            vendLayerTrackGoodsService.setTrackGoodsByLayerIds(layerIds, goodsId)
            map.success = true
            map.message = ""
            //记录日志
            partnerUserOperationLogService.addLog(token, 2 as Short, goodsId, layerIds, 0, 1 as Short)
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 设置轨道商品数量,补货后调用此方法。
     * @param token
     * @param layerIds
     * @return
     */
    def setGoodsNum(String token, String layerIds, Integer num) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            vendLayerTrackGoodsService.setTrackGoodsNum(layerIds, num)
            map.success = true
            map.message = ""
            //记录日志
            partnerUserOperationLogService.addLog(token, 4 as Short, 0, layerIds, 0, 1 as Short)
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 打开指定格子柜门
     * @param token
     * @param trackNos
     */
    def openDoor(String token, String trackNos, Long themeStoreId) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            if (trackNos && trackNos.endsWith(",")) {//去掉结尾的分号
                trackNos = trackNos.substring(0, trackNos.length()-1)
            }
            def themeStore = ThemeStoreBaseInfo.get(themeStoreId)
            if (themeStore) {
                def machine = VendMachineInfo.findByThemeStoreAndIsReal(themeStore, 1 as Short)
                if (machine) {
                    //准备消息推送
                    def pushHelper = new MsgPushHelper()
                    def clientId = machine.clientId
                    def pushParams = [:]
                    pushParams.title = "1"
                    pushParams.data = trackNos
                    pushParams.type = "1"//开柜门
                    String content = pushParams as JSON
                    if (pushHelper.sendMsg(clientId, content)) {
                        //推送成功后记录日志
                        partnerUserOperationLogService.addLog(token, 1 as Short, machine.id, trackNos, 0, 1 as Short)
                        map.status = "000"
                        map.message = "开门成功"
                        map.success = true
                    } else {
                        partnerUserOperationLogService.addLog(token, 1 as Short, machine.id, trackNos, 0, 0 as Short)
                        map.status = "001"
                        map.message = "开门失败"
                        map.success = false
                    }
                } else {
                    map.status = "001"
                    map.message = "开门失败"
                    map.success = false
                }
            } else {
                map.status = "001"
                map.message = "开门失败"
                map.success = false
            }
        } else {
            map.success = false
            map.status = "002"
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 打开所有空柜门
     * @param token
     * @param themeStoreId
     * @param sid 柜子格所在层ID
     */
    def openEmptyDoor(String token,Long themeStoreId,Long sid) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            def themeStore = ThemeStoreBaseInfo.get(themeStoreId)
            if (themeStore) {
                def machine = VendMachineInfo.findByThemeStoreAndIsReal(themeStore, 1 as Short)
                if (machine) {
                    def trackNos = vendLayerTrackGoodsService.getEmptyDoorTrasckNos(sid)
                    if (trackNos == null || trackNos.size() == 0) {
                        map.status = "003"
                        map.message = "没有空柜"
                        map.success = false
                    } else {
                        //准备消息推送
                        def pushHelper = new MsgPushHelper()
                        def clientId = machine.clientId
                        def pushParams = [:]
                        pushParams.title = "1"
                        pushParams.data = trackNos
                        pushParams.type = "1"//开柜门
                        String content = pushParams as JSON
                        if (pushHelper.sendMsg(clientId, content)) {
                            //推送成功后记录日志
                            partnerUserOperationLogService.addLog(token, 0 as Short, machine.id, "", trackNos.size(), 1 as Short)
                            map.status = "000"
                            map.message = "开门成功"
                            map.success = true
                        } else {
                            partnerUserOperationLogService.addLog(token, 0 as Short, machine.id, "", trackNos.size(), 0 as Short)
                            map.status = "001"
                            map.message = "开门失败"
                            map.success = false
                        }
                    }
                } else {
                    map.status = "001"
                    map.message = "开门失败"
                    map.success = false
                }
            } else {
                map.status = "001"
                map.message = "开门失败"
                map.success = false
            }
        } else {
            map.success = false
            map.status = "002"
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 交换商品位置
     * @param token
     * @param data
     * @param themeStoreId
     */
    def changePosition(String token, String data, Long themeStoreId) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            def themeStore = ThemeStoreBaseInfo.get(themeStoreId)
            if (themeStore) {
                def machine = VendMachineInfo.findByThemeStoreAndIsReal(themeStore, 1 as Short)
                if (machine) {
                    try {
                        //数据格式示例 4;17#3;25,4;18#3;26,3;25#4;17,3;26#4;18
                        String[] changeInfoArray = data.split(",")//分割数据,以逗为分割线为一组动作。如4;17#3;25
                        def actionParams = []
                        //解析并生成需要操作的动作序列
                        for(String changeInfo: changeInfoArray) {
                            String oldInfo = changeInfo.split("#")[0]//旧信息,分号前为旧商品ID分号后为vendLayerTrackGoods的id。如4;17
                            String newInfo = changeInfo.split("#")[1]//新信息
                            def layerId = Long.valueOf(oldInfo.split(";")[1])//解析得到vendLayerTrackGoods的id。如17
                            def goodsId = Long.valueOf(newInfo.split(";")[0])//解析得到新商品ID。如3
                            def goodsNum = vendLayerTrackGoodsService.getGoodsNumByLayerId(Long.valueOf(newInfo.split(";")[1]))//解析得到新的数量.从25得到数量。
                            def actionParam = [:]//构建动作参数
                            actionParam.layerId = layerId
                            actionParam.goodsId = goodsId
                            actionParam.goodsNum = goodsNum
                            actionParams.add(actionParam)
                        }
                        //执行生成的动作序列
                        vendLayerTrackGoodsService.updateLayerGoods(actionParams)
                        //记录日志
                        partnerUserOperationLogService.addLog(token, 5 as Short, machine.id, data, actionParams.size(), 1 as Short)
                    } catch (Exception e) {
                        //记录日志
                        partnerUserOperationLogService.addLog(token, 5 as Short, machine.id, data, 0, 1 as Short)
                        e.printStackTrace()
                        map.message = "交换位置失败"
                        map.success = false
                    }
                    map.message = "交换位置成功"
                    map.success = true
                } else {
                    map.message = "交换位置失败"
                    map.success = false
                }
            } else {
                map.message = "交换位置失败"
                map.success = false
            }
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 查询某个大类下的所有商品及数量,需要分页。售卖机购物车需要使用。
     * @param vid 售卖机id
     * @param bid 大类id
     * @param currPage 当前页
     * @param pageSize 每页显示多少
     */
    def queryGoodsByTypeOne(Long vid, Long bid, Integer pageNum, Integer pageSize) {
        def map = [:]
        if (vid && bid && pageNum && pageSize) {
            map = vendLayerTrackGoodsService.queryGoodsByTypeOne(vid, bid, pageNum, pageSize)
        } else {
            map.success = false
            map.msg = "参数不能为空"
        }
        render map as JSON
    }

    //获得指定轨道商品信息
    def getLayerGoods(Long lid) {
        def map = [:]
        def goods = VendLayerTrackGoods.get(lid)
        if (goods) {
            map.sid = goods.id
            map.sno = goods.orbitalNo
            map.goodsName = goods.goods?.name
            map.currentInventory = goods.currentInventory
            map.largestInventory = goods.largestInventory
            map.price = goods.goods?.realPrice
            map.success = true
            map.msg = ""
        } else {
            map.success =false
            map.msg = "指定商品不存在。"
        }
        render map as JSON
    }
}
