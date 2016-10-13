package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.themeStore.ThemeStoreBaseInfo
import grails.converters.JSON

class ShoppingCartController {

    def shoppingCartService

    /**
     * 添加商品到购物车
     * @param openid
     * @param goodsId
     * @return
     */
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

    /**
     * 商品数量减1，当只有一件时则清除该商品。
     * @param openid
     * @param goodsId
     * @return
     */
    def removeGoods(String openid, Long goodsId) {
        def map = [:]
        if (openid && goodsId) {
            shoppingCartService.removeGoods(openid, goodsId)
            map.success = true
            map.message = ""
        } else {
            map.success = false
            map.message = "参数不能为空"
        }
        render map as JSON
    }

    /**
     * 完全删除某种商品，不论有多少件。
     * @param openid
     * @param goodsId
     * @return
     */
    def deleteGoods(String openid, Long goodsId) {
        def map = [:]
        if (openid && goodsId) {
            shoppingCartService.deleteGoods(openid, goodsId)
            map.success = true
            map.message = ""
        } else {
            map.success = false
            map.message = "参数不能为空"
        }
        render map as JSON
    }

    /**
     * 计算购物车里的商品数量及总金额
     * @param openid
     */
    def getGoodsNumAndMoney(String openid) {
        def map = [:]
        if (openid) {
            shoppingCartService.getGoodsNumAndMoney(openid, map)
            map.success = true
            map.message = ""
        } else {
            map.success = false
            map.message = "参数不能为空"
        }
        render map as JSON
    }

    /**
     * 显示购物车内容
     * @param openid
     */
    def showCart(String openid, Long storeId) {
        def cartList = ShoppingCart.findAllByOpenid(openid)
        def store = ThemeStoreBaseInfo.get(storeId)
        render(view: "showCart", model: [cartList: cartList, themeStore: store])
    }
}
