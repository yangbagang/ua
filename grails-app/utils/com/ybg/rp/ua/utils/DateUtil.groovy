package com.ybg.rp.ua.utils

import java.text.SimpleDateFormat

/**
 * Created by yangbagang on 16/7/26.
 */
class DateUtil {

    static sdf_full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    static getFromTime(String startDate) {
        try {
            if (!startDate) {
                startDate = getToday()
            }
            sdf_full.parse("${startDate} 00:00:00")
        } catch (Exception e) {
            getFromTime(getToday())
        }
    }

    static getToTime(String endDate) {
        try {
            if (!endDate) {
                endDate = getToday()
            }
            sdf_full.parse("${endDate} 23:59:59")
        } catch (Exception e) {
            getToTime(getToday())
        }
    }

    static getToday() {
        def now = sdf_full.format(new Date())
        now.split(" ")[0]
    }

    static getNow() {
        def now = sdf_full.format(new Date())
        now.split(" ")[1]
    }
}
