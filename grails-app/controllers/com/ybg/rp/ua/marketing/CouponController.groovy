package com.ybg.rp.ua.marketing

import grails.converters.JSON

class CouponController {

    /**
     * 检查指定优惠卷是否有效
     * @param code
     * @return
     */
    def check(String code) {
        def map = [:]
        if (code) {
            def coupon = Coupon.findByCode(code)
            if (coupon) {
                if (coupon.flag == Short.valueOf("1")) {
                    map.success = true
                    map.msg = ""
                    map.coupon= coupon
                } else {
                    map.success = false
                    map.msg = "编号己经失效"
                }
            } else {
                map.success = false
                map.msg = "编号不存在"
            }
        } else {
            map.success = false
            map.msg = "编号不能为空"
        }
        render map as JSON
    }

}