package com.ybg.rp.ua.utils

import com.ybg.rp.ua.marketing.Coupon

/**
 * Created by yangbagang on 2016/12/30.
 */
class CouponUtil {

    static checkIsValid(Coupon coupon) {
        if (coupon == null) {
            return false
        }
        def calendar = Calendar.getInstance(Locale.CHINA)
        if (coupon.dayOfWeek != 0 && coupon.dayOfWeek != calendar.get(Calendar.DAY_OF_WEEK)) {
            return false
        }

        if (coupon.dayOfMonth != 0 && coupon.dayOfMonth != calendar.get(Calendar.DAY_OF_MONTH)) {
            return false
        }

        if (coupon.maxCount < 1) {
            return false
        }
        return true
    }
}
