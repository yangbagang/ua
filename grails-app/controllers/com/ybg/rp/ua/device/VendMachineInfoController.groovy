package com.ybg.rp.ua.device

import grails.converters.JSON

class VendMachineInfoController {

    def updateClientIdByVmCode(String vmCode, String clientId) {
        def map = [:]
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

    def addErrorInfo(Long machineId, String orbitalNo, String errorMsg) {
        def map = [:]
        def machine = VendMachineInfo.get(machineId)
        if (machine) {
            //create a record
            def errorInfo = new VendMachineInfoErrorInfo()
            errorInfo.vendMachine = machine
            errorInfo.orbitalNo = orbitalNo
            errorInfo.createTime = new Date()
            errorInfo.errorInfo = errorMsg
            errorInfo.status = 0 as Short
            errorInfo.save flush: true

            //update status
            def layer = VendLayerTrackGoods.findByVendMachineAndOrbitalNo(machine, orbitalNo)
            layer.workStatus = 1 as Short
            layer.sellStatus = 1 as Short
            layer.save flush: true

            //set result
            map.success = true
            map.msg = "更新成功"
        } else {
            //The machine was not found.
            map.success = false
            map.msg = "指定售卖机不存在"
        }
        render map as JSON
    }

}
