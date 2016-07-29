package com.ybg.rp.ua.utils

/**
 * Created by yangbagang on 16/7/29.
 */
class NumberUtil {

    static getListFromString(String ids) {
        def list = []
        try {
            if (ids) {
                String[] idList = ids.split(",")
                for (String id: idList) {
                    if (id != null && id != "") {
                        list.add(Long.valueOf(id))
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        list
    }
}
