package com.ybg.rp.ua.marketing

import com.ybg.rp.ua.utils.CouponUtil
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
                    if (!CouponUtil.checkIsValid(coupon)) {
                        map.success = false
                        map.msg = "当前不可用"
                    } else {
                        map.success = true
                        map.msg = ""
                        map.coupon= coupon
                    }
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

    def check2() {
        def calendar1 = Calendar.getInstance(Locale.CHINA)
        def calendar2 = Calendar.getInstance(Locale.default)
        def calendar3 = Calendar.getInstance(Locale.CHINESE)
        def w1 = calendar1.get(Calendar.DAY_OF_WEEK)
        def w2 = calendar2.get(Calendar.DAY_OF_WEEK)
        def w3 = calendar3.get(Calendar.DAY_OF_WEEK)
        println "DAY_OF_WEEK in china is: ${w1}"
        println "DAY_OF_WEEK in default is: ${w2}"
        println "DAY_OF_WEEK in chinese is: ${w3}"
        render "in china ${w1}, in default ${w2}, in chinese ${w3}"
    }

}
