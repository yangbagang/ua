package com.ybg.rp.ua.device

import grails.converters.JSON

class VendMachineInfoController {

    def updateClientIdByVmCode(String vmCode, String clientId) {
        def map = [""]
        def vendMachinaInfo = VendMachineInfo.findByMachineCode(vmCode)
        if (vendMachinaInfo) {
            vendMachinaInfo.clientId = clientId
            vendMachinaInfo.save flush: true
            map.success = true
            map.machineId = vendMachineInfo.id
            map.msg = "更新成功"
        } else {
            map.success = false
            map.machineId = 0
            map.msg = "指定编号不存在"
        }
        render map as JSON
    }
}
