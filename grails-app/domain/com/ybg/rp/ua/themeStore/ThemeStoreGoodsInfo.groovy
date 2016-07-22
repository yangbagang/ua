package com.ybg.rp.ua.themeStore

class ThemeStoreGoodsInfo {

    static belongsTo = [themeStore: ThemeStoreBaseInfo]

    static constraints = {
        picId nullable: true
        px nullable: true
    }

    Long baseId
    String name
    String brand
    String specifications
    Float basePrice
    Float realPrice
    String picId
    Short status
    Integer px
    String letter
}
