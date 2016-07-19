package com.ybg.rp.ua.device

import com.ybg.rp.ua.themeStore.ThemeStoreGoodsInfo

class VendLayerTrackGoods {

    static belongsTo = [vendMachine: VendMachineInfo ]

    static constraints = {
        goods nullable: true
        sellStatus nullable: true
        currentInventory nullable: true
        largestInventory nullable: true
        workStatus nullable: true
    }

    /**轨道编号*/
    String orbitalNo

    /**第几层（层数）*/
    String layer

    ThemeStoreGoodsInfo goods//在售商品

    /**在售状态(1：是, 0：否)*/
    Short sellStatus

    /**当前库存*/
    Integer currentInventory

    /**最大库存*/
    Integer largestInventory

    /**轨道工作状态1：良好, 0：损坏*/
    Short workStatus
}
