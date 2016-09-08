package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.device.VendLayerTrackGoods
import com.ybg.rp.ua.device.VendMachineInfo
import com.ybg.rp.ua.partner.PartnerBaseInfo
import com.ybg.rp.ua.themeStore.ThemeStoreBaseInfo
import com.ybg.rp.ua.themeStore.ThemeStoreGoodsInfo
import com.ybg.rp.ua.themeStore.ThemeStoreOfPartner
import com.ybg.rp.ua.utils.DateUtil
import com.ybg.rp.ua.utils.OrderUtil
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import groovy.transform.Synchronized

@Transactional
class OrderInfoService {

    def deliveryGoodsFail(String orderSn, String trackNo, Long gid) {
        def orderInfo = OrderInfo.findByOrderNo(orderSn)
        def goodsInfo = VendLayerTrackGoods.get(gid)
        if (orderInfo && goodsInfo) {
            def orderDetails = OrderDetail.findAllByOrderAndGoodsAndOrbitalNo(orderInfo, goodsInfo, trackNo)
            for (OrderDetail orderDetail : orderDetails) {
                orderDetail.status = 0 as Short
                orderDetail.errorStatus = 1 as Short
                orderDetail.save flush: true
            }
            goodsInfo.workStatus = 1 as Short
            goodsInfo.sellStatus = 1 as Short
            goodsInfo.save flush: true
        }
    }

    @Synchronized
    def updateCurrentInventory(OrderInfo orderInfo) {
        if (orderInfo) {
            def details = OrderDetail.findAllByOrder(orderInfo)
            for (OrderDetail detail : details) {
                if (detail.status != (3 as Short)) {//防止重复扣库存
                    //update num
                    def layer = detail.goods
                    if (layer.currentInventory >= detail.goodsNum) {
                        layer.currentInventory = layer.currentInventory - detail.goodsNum
                        layer.save flush: true
                    }
                    //update status
                    detail.status = 3 as Short
                    detail.errorStatus = 0 as Short
                    detail.save flush: true
                }
            }
        }
    }

    def createOrderWithSingleGoods(Long machineId, String trackNo) {
        def map = [:]
        def machine = VendMachineInfo.get(machineId)
        if (machine) {
            def layer = VendLayerTrackGoods.findByVendMachineAndOrbitalNo(machine, trackNo)
            if (layer) {
                if (layer.currentInventory > 0) {
                    //创建订单
                    def orderInfo = new OrderInfo()
                    orderInfo.vendMachine = machine
                    orderInfo.orderNo = OrderUtil.createOrderNo()
                    orderInfo.createTime = new Date()
                    orderInfo.orderMoney = layer.goods.realPrice
                    orderInfo.totalMoney = layer.goods.realPrice
                    orderInfo.realMoney = layer.goods.realPrice
                    orderInfo.transNo = ""
                    orderInfo.save flush: true
                    //创建订单详情
                    def orderDetail = new OrderDetail()
                    orderDetail.order = orderInfo
                    orderDetail.goods = layer
                    orderDetail.goodsNum = 1
                    orderDetail.goodsPrice = layer.goods.realPrice
                    orderDetail.discount = 0
                    orderDetail.buyPrice = orderDetail.goodsPrice
                    orderDetail.goodsName = layer.goods.name
                    orderDetail.goodsSpec = layer.goods.specifications
                    orderDetail.goodsPic = layer.goods.picId
                    orderDetail.orbitalNo = layer.orbitalNo
                    orderDetail.status = 0 as Short
                    orderDetail.errorStatus = 0 as Short
                    orderDetail.refundPrice = 0
                    orderDetail.save flush: true
                    if (orderDetail.errors) {
                        orderDetail.errors.each {
                            println it
                        }
                    }
                    //构造返回结果
                    def goodsInfo = [:]//商品信息
                    goodsInfo.gid = layer.goods.id
                    goodsInfo.goodsPic = layer.goods.picId
                    goodsInfo.goodsName = layer.goods.name
                    goodsInfo.price = layer.goods.realPrice
                    goodsInfo.goodsDesc = layer.goods.specifications
                    goodsInfo.kucun = 1
                    goodsInfo.num = 1
                    goodsInfo.trackNo = layer.orbitalNo
                    def order = [:]//订单信息
                    order.orderNo = orderInfo.orderNo
                    order.orderMoney = orderInfo.realMoney
                    order.orderInfos = [goodsInfo]//二选一,新版上线后删除此行。//TODO
                    order.goodsInfo = [goodsInfo]
                    //返回结果
                    map.orderInfo = order
                    map.success = true
                    map.msg = "下单成功"
                } else {
                    map.success = false
                    map.msg = "库存不足"
                }
            } else {
                map.success = false
                map.msg = "指定编号不存在"
            }
        } else {
            map.success = false
            map.msg = "指定售卖机不存在"
        }
        return map
    }

    def createOrder(Long machineId, String goodsJson) {
        def map = [:]
        def machine = VendMachineInfo.get(machineId)
        if (machine) {
            if (goodsJson) {
                def jsonSlurper = new JsonSlurper()
                def goodsList = jsonSlurper.parseText(goodsJson)
                //创建订单
                def orderInfo = new OrderInfo()
                orderInfo.vendMachine = machine
                orderInfo.orderNo = OrderUtil.createOrderNo()
                orderInfo.createTime = new Date()
                orderInfo.orderMoney = 0
                orderInfo.totalMoney = 0
                orderInfo.realMoney = 0
                orderInfo.transNo = ""
                def goodsVoList = []
                goodsList.each {
                    def remainNum = Integer.valueOf(it.num)
                    def goods = ThemeStoreGoodsInfo.get(Long.valueOf(it.gid))
                    def layers = VendLayerTrackGoods.findAllByVendMachineAndGoods(machine, goods)
                    for (VendLayerTrackGoods layer: layers) {
                        if (layer.currentInventory < 1) {
                            continue//跳过空轨道
                        }
                        def takeNum = Math.min(layer.currentInventory, remainNum)
                        //创建订单详情
                        def orderDetail = new OrderDetail()
                        orderDetail.order = orderInfo
                        orderDetail.goods = layer
                        orderDetail.goodsNum = takeNum
                        orderDetail.goodsPrice = layer.goods.realPrice * takeNum
                        orderDetail.discount = 0
                        orderDetail.buyPrice = layer.goods.realPrice * takeNum
                        orderDetail.goodsName = layer.goods.name
                        orderDetail.goodsSpec = layer.goods.specifications
                        orderDetail.goodsPic = layer.goods.picId
                        orderDetail.orbitalNo = layer.orbitalNo
                        orderDetail.status = 0 as Short
                        orderDetail.errorStatus = 0 as Short
                        orderDetail.refundPrice = 0
                        orderDetail.save flush: true
                        //累加订单金额
                        orderInfo.orderMoney += orderDetail.buyPrice
                        orderInfo.totalMoney += orderDetail.buyPrice
                        orderInfo.realMoney += orderDetail.buyPrice
                        //构造返回结果
                        def goodsInfo = [:]//商品信息
                        goodsInfo.gid = layer.goods.id
                        goodsInfo.goodsPic = layer.goods.picId
                        goodsInfo.goodsName = layer.goods.name
                        goodsInfo.price = layer.goods.realPrice
                        goodsInfo.goodsDesc = layer.goods.specifications
                        goodsInfo.kucun = takeNum
                        goodsInfo.num = takeNum
                        goodsInfo.trackNo = layer.orbitalNo
                        goodsVoList.add(goodsInfo)
                        //检查数量
                        remainNum = remainNum - takeNum
                        if (remainNum < 1) {
                            break//己经分配完,跳出循环,进行下一个商品分配。
                        }
                    }
                }
                orderInfo.save flush: true
                //订单信息
                def order = [:]
                order.orderNo = orderInfo.orderNo
                order.orderMoney = orderInfo.realMoney
                order.orderInfos = goodsVoList//二选一,新版上线后删除此行。//TODO
                order.goodsInfo = goodsVoList
                //返回结果
                map.orderInfo = order
                map.success = true
                map.msg = "下单成功"
            } else {
                map.success = false
                map.msg = "参数不能为空"
            }
        } else {
            map.success = false
            map.msg = "指定售卖机不存在"
        }
        return map
    }

    def updateOrderStatus(OrderInfo orderInfo, TransactionInfo transactionInfo, String transaction_no) {
        orderInfo.transNo = transaction_no
        orderInfo.payStatus = 1 as Short
        orderInfo.confirmTime = new Date()
        orderInfo.payWay = Short.valueOf(transactionInfo.payType)
        transactionInfo.isSuccess = 1 as Short
        transactionInfo.updateTime = new Date()
        transactionInfo.save flush: true
        orderInfo.save flush: true
    }

    def countMoney(PartnerBaseInfo partner, String themeIds, String startDate, String endDate) {
        //查询范围
        def themeStores
        if (themeIds == null || "" == themeIds) {
            themeStores = ThemeStoreOfPartner.findAllByPartner(partner)
        } else {
            themeStores = getThemeStores(themeIds)
        }

        def c = OrderInfo.createCriteria()
        def money = c.get {
            projections {
                sum("realMoney")
            }
            and {
                vendMachine{
                    'in'("themeStore", themeStores)
                }
                gt("completeTime", DateUtil.getFromTime(startDate))
                lt("completeTime", DateUtil.getToTime(endDate))
                eq("payStatus", 1 as Short)
            }
        }
        if (money) {
            return money.round(2)
        }
        return 0
    }

    def countOrderNum(PartnerBaseInfo partner, String themeIds, String startDate, String endDate) {
        //查询范围
        def themeStores
        if (themeIds == null || "" == themeIds) {
            themeStores = ThemeStoreOfPartner.findAllByPartner(partner)
        } else {
            themeStores = getThemeStores(themeIds)
        }

        def c = OrderInfo.createCriteria()
        def count = c.get {
            projections {
                count("orderNo")
            }
            and {
                vendMachine{
                    'in'("themeStore", themeStores)
                }
                gt("completeTime", DateUtil.getFromTime(startDate))
                lt("completeTime", DateUtil.getToTime(endDate))
                eq("payStatus", 1 as Short)
            }
        }
        count
    }

    private getThemeStores(String themeIds) {
        def ids = []
        def tIds = themeIds.split(",")
        for (String id: tIds) {
            if ("" != id) {
                try {
                    ids.add(Long.valueOf(id))
                } catch (Exception e) {
                    e.printStackTrace()
                    //nothing
                }
            }
        }
        ThemeStoreBaseInfo.findAllByIdInList(ids)
    }
}
