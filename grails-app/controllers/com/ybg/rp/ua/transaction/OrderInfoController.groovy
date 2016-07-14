package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.device.VendLayerTrackGoods
import com.ybg.rp.ua.utils.OrderUtil
import grails.converters.JSON
import grails.transaction.Transactional

import com.ybg.rp.ua.device.VendMachineInfo

import java.text.SimpleDateFormat

class OrderInfoController {

    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    def orderInfoService

    @Transactional
    def updateOrderStatus(String orderNo, String result, String payType, String transDate, boolean isDataUp) {
        def map = [:]
        def orderInfo = OrderInfo.findByOrderNo(orderNo)
        if (!orderInfo) {
            map.success = false
            map.msg = "没有该订单数据-${orderNo}"
            log.info("没有该订单数据-${orderNo}")
            render map as JSON
            return
        }
        if ("0".equals(result)) {//取消订单
            if (orderInfo.transNo) {
                orderInfo.deliveryStatus = 1 as Short
            } else {
                if (orderInfo.payStatus == 1) {
                    map.success = true
                    map.msg = "接收成功"
                    render map as JSON
                    return
                }
                if (orderInfo.isCancel == 0) {
                    orderInfo.isCancel == 1
                }
            }
        } else if ("1".equals(result)) {//出货成功
            orderInfo.deliveryStatus = 1 as Short
            orderInfo.completeTime = sdf.parse(transDate)
            if (isDataUp) {
                orderInfoService.updateCurrentInventory(orderInfo)
            }
        } else if ("2".equals(result)) {//出货失败
            orderInfo.deliveryStatus = 2 as Short
        }
        orderInfo.save flush: true
        map.success = true
        map.msg = "接收成功"
        render map as JSON
    }

    @Transactional
    def deliveryGoodsFail(String orderSn, String trackNo, Long gid) {
        orderInfoService.deliveryGoodsFail(orderSn, trackNo, gid)

        def map = [:]
        map.success = true
        map.msg = "上传成功"
        render map as JSON
    }

    @Transactional
    def createOrderWithMachineIdAndTrackNo(Long machineId, String trackNo) {
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
                    map.orderNo = orderInfo.orderNo
                    map.goodId = layer.goods.id
                    map.goodPrice = orderDetail.buyPrice
                    map.goodName = orderDetail.goodsName
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
        render map as JSON
    }
}
