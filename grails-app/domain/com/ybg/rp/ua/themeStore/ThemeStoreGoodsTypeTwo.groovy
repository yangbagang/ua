package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.base.GoodsTypeTwo

class ThemeStoreGoodsTypeTwo {

    static belongsTo = [goodsInfo: ThemeStoreGoodsInfo, typeTwo: GoodsTypeTwo]

    static constraints = {
    }

    static ThemeStoreGoodsTypeTwo createInstance(ThemeStoreGoodsInfo goodsInfo, GoodsTypeTwo goodsTypeTwo) {
        def instance = new ThemeStoreGoodsTypeTwo(goodsInfo: goodsInfo, typeTwo: goodsTypeTwo)
        instance.save flush: true
        instance
    }
}
