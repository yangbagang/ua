package com.ybg.rp.ua.device

class VendLayerTrackInfo {

    static belongsTo = [vendMachine: VendMachineInfo]

    static constraints = {
    }

    /**第几层（层数）*/
    String layer

    /** 轨道数量*/
    Integer orbitalNum

    Short isCabinet = 0

}
