package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.base.GoodsTypeTwo
import com.ybg.rp.ua.device.VendLayerTrackGoods
import com.ybg.rp.ua.device.VendMachineInfo
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON

class ThemeStoreGoodsInfoController {

    def themeStoreGoodsInfoService

    def listGoodsByTypeTwo(Long machineId, Long typeTwoId) {
        def machine = VendMachineInfo.get(machineId)//未来扩展用
        def typeTwo = GoodsTypeTwo.get(typeTwoId)
        def map = [:]
        map.success = true
        map.list = ThemeStoreGoodsTypeTwo.findAllByTypeTwo(typeTwo)*.goodsInfo
        render map as JSON
    }

    def listGoodsNum(Long machineId, Long goodsId) {
        def machine = VendMachineInfo.get(machineId)
        def goods = ThemeStoreGoodsInfo.get(goodsId)
        def num = 0
        def list = VendLayerTrackGoods.findAllByVendMachineAndGoods(machine, goods)
        for (VendLayerTrackGoods g : list) {
            num += g.currentInventory
        }
        render num
    }

    /**
     * 查询某个主题店符合关键词的所有在售商品。APP补货用。
     * @param token
     * @param themeStoreId
     * @param keyWord
     * @return
     */
    def queryGoodsByName(String token, Long themeStoreId, String keyWord) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            def themeStore = ThemeStoreBaseInfo.get(themeStoreId)
            if (themeStore) {
                def dataList = themeStoreGoodsInfoService.listGoodsByName(themeStore, keyWord)
                map.dataList = dataList
                map.success = true
            } else {
                map.success = false
                map.message = "主题店不存在"
            }
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 查询某个主题店所有在售商品。APP补货用。
     * @param token
     * @param themeStoreId
     * @param keyWord
     * @return
     */
    def queryGoods(String token, Long themeStoreId) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            def themeStore = ThemeStoreBaseInfo.get(themeStoreId)
            if (themeStore) {
                def dataList = themeStoreGoodsInfoService.listGoods(themeStore)
                map.dataList = dataList
                map.success = true
            } else {
                map.success = false
                map.message = "主题店不存在"
            }
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 查询某个主题店的所有在售商品。APP补货用。
     * @param token
     * @param themeStoreId
     * @return
     */
    def queryGoodsByThemeStoreId(String token, Long themeStoreId) {
        queryGoodsByName(token, themeStoreId, null)
    }

}
