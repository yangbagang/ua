package com.ybg.rp.ua.utils

import java.text.SimpleDateFormat

/**
 * Created by yangbagang on 16/7/14.
 */
class OrderUtil {

    public static String createOrderNo() {
        def sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS")
        sdf.format(new Date())
    }
}
