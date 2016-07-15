package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.device.VendLayerTrackGoods
import com.ybg.rp.ua.device.VendMachineInfo
import com.ybg.rp.ua.utils.OrderUtil
import grails.transaction.Transactional

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

    def updateCurrentInventory(OrderInfo orderInfo) {
        if (orderInfo) {
            def details = OrderDetail.findAllByOrder(orderInfo)
            for (OrderDetail detail : details) {
                //update num
                def layer = detail.goods
                if (layer.currentInventory > detail.goodsNum) {
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

    def createOrderWithSingleGoods(Long machineId, String trackNo) {
        def map = [:]
        def machine = VendMachineInfo.get(machineId)
        if (machine) {
            def layer = VendLayerTrackGoods.findByVendMachineAndOrbitalNo(machine, trackNo)
            if (layer) {
                if (layer.currentInventory > 0) {
                    //create a order
                    def orderInfo = new OrderInfo()
                    orderInfo.vendMachine = machine
                    orderInfo.orderNo = OrderUtil.createOrderNo()
                    orderInfo.createTime = new Date()
                    orderInfo.orderMoney = layer.goods.realPrice
                    orderInfo.totalMoney = layer.goods.realPrice
                    orderInfo.realMoney = layer.goods.realPrice
                    orderInfo.save flush: true
                    //create a order detail
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
                    //create result
                    map.orderInfo = orderInfo
                    map.detail = [orderDetail]
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
        return map
    }
}
