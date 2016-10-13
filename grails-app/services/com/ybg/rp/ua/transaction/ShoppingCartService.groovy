package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.themeStore.ThemeStoreGoodsInfo
import grails.transaction.Transactional

@Transactional
class ShoppingCartService {

    /**
     * 添加商品至购物车
     * @param openid
     * @param goodsId
     * @return
     */
    def addGoods(String openid, Long goodsId) {
        def goods = ThemeStoreGoodsInfo.get(goodsId)
        if (goods) {
            def cart = ShoppingCart.findByOpenidAndGoodsInfo(openid, goods)
            if (cart) {
                cart.num = cart.num + 1
                cart.save flush: true
            } else {
                cart = new ShoppingCart()
                cart.num = 1
                cart.openid = openid
                cart.goodsInfo = goods
                cart.save flush: true
            }
        }
    }

    def getGoodsNumAndMoney(String openid, Map map) {
        def num = 0
        def money = 0d
        def cartList = listGoods(openid)
        cartList.each { cart ->
            num += cart.num
            money += cart.num * cart.goodsInfo.realPrice
        }
        map.num = num
        map.money = money.round(2)
    }

    /**
     * 列出购物车商品
     * @param openid
     * @return
     */
    def listGoods(String openid) {
        ShoppingCart.findAllByOpenid(openid)
    }

    /**
     * 移除一件商品
     * @param openid
     * @param goodsId
     * @return
     */
    def removeGoods(String openid, Long goodsId) {
        def goods = ThemeStoreGoodsInfo.get(goodsId)
        if (goods) {
            def cart = ShoppingCart.findByOpenidAndGoodsInfo(openid, goods)
            if (cart) {
                if (cart.num > 1) {
                    cart.num = cart.num - 1
                    cart.save flush: true
                } else {
                    cart.delete flush: true
                }
            }
        }
    }

    /**
     * 移除某个商品，不论多少件完全清除。
     * @param openid
     * @param goodsId
     * @return
     */
    def deleteGoods(String openid, Long goodsId) {
        def goods = ThemeStoreGoodsInfo.get(goodsId)
        if (goods) {
            def cart = ShoppingCart.findByOpenidAndGoodsInfo(openid, goods)
            if (cart) {
                cart.delete flush: true
            }
        }
    }

    /**
     * 清空购物车
     * @param openid
     * @return
     */
    def clearCart(String openid) {
        def cartList = ShoppingCart.findAllByOpenid(openid)
        cartList.each { cart ->
            cart.delete flush: true
        }
    }
}
