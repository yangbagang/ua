package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.base.GoodsTypeLabel

class ThemeStoreGoodsLabel {

    static belongsTo = [goodsInfo: ThemeStoreGoodsInfo, goodsLabel: GoodsTypeLabel]

    static constraints = {
    }

    static ThemeStoreGoodsLabel createInstance(ThemeStoreGoodsInfo goodsInfo, GoodsTypeLabel goodsTypeLabel) {
        def instance = new ThemeStoreGoodsLabel(goodsInfo: goodsInfo, goodsTypeLabel: goodsTypeLabel)
        instance.save flush: true
        instance
    }
}
