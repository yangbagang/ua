package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.base.GoodsTypeTwo
import com.ybg.rp.ua.device.VendLayerTrackGoods
import com.ybg.rp.ua.device.VendMachineInfo
import grails.converters.JSON

class ThemeStoreGoodsInfoController {

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
}
