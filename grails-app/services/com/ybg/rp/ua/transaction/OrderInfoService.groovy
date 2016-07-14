package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.device.VendLayerTrackGoods
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
}
