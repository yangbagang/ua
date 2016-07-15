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
    def updateOrderStatus(String orderNo, String result, String payType, String transDate, boolean isDataUp, Short cancelType) {
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
                    if (cancelType != 0) {
                        orderInfo.isCancel = cancelType
                    } else {
                        orderInfo.isCancel = 1 as Short
                    }
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
        def map = orderInfoService.createOrderWithSingleGoods(machineId, trackNo)
        render map as JSON
    }

    @Transactional
    def createOrderWithMchineIdAndGoodsJson(Long machine, String goodsJson) {
        def map = orderInfoService.createOrder(machine, goodsJson)
        render map as JSON
    }
}
