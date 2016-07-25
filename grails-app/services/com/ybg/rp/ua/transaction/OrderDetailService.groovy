package com.ybg.rp.ua.transaction

import grails.transaction.Transactional

@Transactional
class OrderDetailService {

    def updateStatus(OrderInfo orderInfo, Short status) {
        if (orderInfo) {
            def details = OrderDetail.findAllByOrder(orderInfo)
            for (OrderDetail detail: details) {
                detail.status = status
                detail.save flush: true
            }
        }
    }
}
