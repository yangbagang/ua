package com.ybg.rp.ua.transaction

import grails.converters.JSON

class ShoppingCartController {

    def shoppingCartService

    def addGoods(String openid, Long goodsId) {
        def map = [:]
        if (openid && goodsId) {
            shoppingCartService.addGoods(openid, goodsId)
            map.success = true
            map.message = ""
        } else {
            map.success = false
            map.message = "参数不能为空"
        }
        render map as JSON
    }
}
