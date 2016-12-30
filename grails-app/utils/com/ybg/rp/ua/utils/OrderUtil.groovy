package com.ybg.rp.ua.utils

import java.text.SimpleDateFormat

/**
 * Created by yangbagang on 16/7/14.
 */
class OrderUtil {

    static String createOrderNo() {
        def sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS")
        sdf.format(new Date())
    }
}
