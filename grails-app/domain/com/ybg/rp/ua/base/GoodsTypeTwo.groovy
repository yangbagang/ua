package com.ybg.rp.ua.base

class GoodsTypeTwo {

    static belongsTo = [typeOne: GoodsTypeOne]

    static constraints = {

    }

    String name
    Short status

}
