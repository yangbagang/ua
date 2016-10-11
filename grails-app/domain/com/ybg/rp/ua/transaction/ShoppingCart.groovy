package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.themeStore.ThemeStoreGoodsInfo

class ShoppingCart {

    static constraints = {
    }

    //微信购物车
    String openid
    ThemeStoreGoodsInfo goodsInfo
    Integer num
}
