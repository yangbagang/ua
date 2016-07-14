package com.ybg.rp.ua.device

import com.ybg.rp.ua.themeStore.ThemeStoreGoodsInfo

class VendLayerTrackGoods {

    static belongsTo = [vendMachine: VendMachineInfo ]

    static constraints = {
    }

    /**轨道编号*/
    String orbitalNo

    /**第几层（层数）*/
    String layer

    ThemeStoreGoodsInfo goods//在售商品

    /**在售状态(0：是,1：否)*/
    Short sellStatus

    /**当前库存*/
    Integer currentInventory

    /**最大库存*/
    Integer largestInventory

    /**轨道工作状态0：良好,1：损坏*/
    Short workStatus
}
