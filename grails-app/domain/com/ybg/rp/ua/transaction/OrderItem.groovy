package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.themeStore.ThemeStoreGoodsInfo

class OrderItem {

    static belongsTo = [order: OrderInfo]

    static constraints = {
    }

    String openid
    ThemeStoreGoodsInfo goodsInfo
    Integer num
}
