package com.ybg.rp.ua.device

import com.ybg.rp.ua.base.PushMsgBaseVo
import com.ybg.rp.ua.partner.LoginPushVo
import com.ybg.rp.ua.partner.PartnerUserInfo
import com.ybg.rp.ua.themeStore.ThemeStoreBaseInfo
import com.ybg.rp.ua.themeStore.ThemeStoreOfPartner
import com.ybg.rp.ua.utils.MsgPushHelper
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON
import groovy.json.JsonSlurper

class VendMachineInfoController {

    /**
     * 更新售卖机clientId及在线状态
     * @param vmCode 机器编号
     * @param clientId 个推ID
     * @return
     */
    def updateClientIdByVmCode(String vmCode, String clientId) {
        def map = [:]
        def vendMachineInfo = VendMachineInfo.findByMachineCode(vmCode)
        if (vendMachineInfo) {
            vendMachineInfo.clientId = clientId
            vendMachineInfo.onlineStatus = 1 as Short
            vendMachineInfo.reportTime = new Date()
            vendMachineInfo.save flush: true
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

    /**
     * 初始化售卖机数据,设定售卖机有多少层,格子柜总体算一层。
     * @param machineId
     * @param operatorId
     * @param layers JSON格式数据
     * @return
     */
    def updateLayerNo(Long machineId, Long operatorId, String layers) {//operatorId暂时不用
        println "updateLayerNo: machineId=${machineId}, layers=${layers}"
        def map = [:]
        def vendMachineInfo = VendMachineInfo.get(machineId)
        if (vendMachineInfo) {
            if (layers != null && !"".equals(layers)) {
                def jsonSlurper = new JsonSlurper()
                def layerVOs = jsonSlurper.parseText(layers)
                if (layerVOs) {
                    //查询该售卖机下的轨道
                    VendLayerTrackInfo.findAllByVendMachine(vendMachineInfo)?.each {
                        it.delete flush: true
                    }
                    //新建
                    layerVOs.each {
                        def layer = new VendLayerTrackInfo()
                        layer.vendMachine = vendMachineInfo
                        layer.layer = it.layerNo
                        layer.orbitalNum = it.trackNum
                        if (it.layerNo in ["1", "2", "3", "4", "5", "6"]) {//特殊数字,设定为格子柜。
                            layer.isCabinet = 1 as Short
                        } else {
                            def match = ~/(A|B|C|D).*/
                            if (it.layerNo ==~ match) {//字母,设定为副柜。
                                layer.isCabinet = 2 as Short
                            } else {
                                layer.isCabinet = 0 as Short
                            }
                        }
                        layer.save flush: true
                    }
                    map.success = true
                    map.msg = "更新成功"
                } else {
                    map.success = false
                    map.msg = "轨道参数不能为空"
                }
            } else {
                map.success = false
                map.msg = "轨道参数不能为空"
            }
        } else {
            map.success = false
            map.msg = "售卖机不存在"
        }
        println map
        render map as JSON
    }

    /**
     * 初始化数据,设定每个轨道最大售货量及编号。编号由售卖机APP(VM项目)中生成。
     * @param machineId 机器ID
     * @param operatorId
     * @param layerNo 轨道号
     * @param gsons JSON格式的数据
     * @return
     */
    def updateMaxNum(Long machineId, Long operatorId, String layerNo, String gsons) {//operatorId暂时不用
        println "updateMaxNum: machineId=${machineId}, layerNo=${layerNo}, gsons=${gsons}"
        def map = [:]
        def vendMachineInfo = VendMachineInfo.get(machineId)
        if (vendMachineInfo && layerNo) {
            if (gsons != null && !"".equals(gsons)) {
                def jsonSlurper = new JsonSlurper()
                def layerVOs = jsonSlurper.parseText(gsons)
                if (layerVOs) {
                    //获取该层所有数据.TODO当某层轨道数减少时会出现异常
                    //def trackGoods = VendLayerTrackGoods.findAllByVendMachineAndLayer(vendMachineInfo, layerNo)
                    //检查该层数据
                    layerVOs.each {
                        def goods = VendLayerTrackGoods.findByVendMachineAndOrbitalNo(vendMachineInfo, it.trackNo)
                        if (!goods) {
                            goods = new VendLayerTrackGoods()
                            goods.vendMachine = vendMachineInfo
                            goods.orbitalNo = it.trackNo
                        }
                        goods.layer = layerNo
                        goods.sellStatus = 1 as Short
                        goods.currentInventory = 0
                        goods.largestInventory = it.maxInventory
                        goods.workStatus = 1 as Short
                        if (it.layerNo in ["1", "2", "3", "4", "5", "6"]) {//特殊数字,设定为格子柜。
                            goods.isCabinet = 1 as Short
                        } else {
                            def match = ~/(A|B|C|D).*/
                            if (it.layerNo ==~ match) {//字母,设定为副柜。
                                goods.isCabinet = 2 as Short
                            } else {
                                goods.isCabinet = 0 as Short
                            }
                        }
                        goods.save flush: true
                    }
                    map.success = true
                    map.msg = "更新成功"
                } else {
                    map.success = false
                    map.msg = "参数不能为空"
                }
            } else {
                map.success = false
                map.msg = "参数不能为空"
            }
        } else {
            map.success = false
            map.msg = "参数错误"
        }
        println map
        render map as JSON
    }

    /**
     * 增加错误信息,售卖机轨道出错时调用此方法。
     * @param machineId
     * @param orbitalNo
     * @param errorMsg
     * @return
     */
    def addErrorInfo(Long machineId, String orbitalNo, String errorMsg) {
        def map = [:]
        def machine = VendMachineInfo.get(machineId)
        if (machine) {
            //将售卖机标记为出错
            machine.status = 0 as Short
            machine.save flush: true

            //创建出错信息
            def errorInfo = new VendMachineInfoErrorInfo()
            errorInfo.vendMachine = machine
            errorInfo.orbitalNo = orbitalNo
            errorInfo.createTime = new Date()
            errorInfo.errorInfo = errorMsg
            errorInfo.status = 0 as Short
            errorInfo.save flush: true

            //更新状态
            def layer = VendLayerTrackGoods.findByVendMachineAndOrbitalNo(machine, orbitalNo)
            layer.workStatus = 0 as Short
            layer.sellStatus = 0 as Short
            layer.save flush: true

            //构造返回结果
            map.success = true
            map.msg = "更新成功"
        } else {
            //The machine was not found.
            map.success = false
            map.msg = "指定售卖机不存在"
        }
        render map as JSON
    }

    /**
     * 修复轨道错误
     * @param machineId
     * @param orbitalNo
     * @return
     */
    def fixError(Long machineId, String orbitalNo) {
        def map = [:]
        def machine = VendMachineInfo.get(machineId)
        if (machine) {
            //更新错误记录
            def errorInfo = VendMachineInfoErrorInfo.findByVendMachineAndOrbitalNoAndStatus(machine, orbitalNo, 0 as Short)
            if (errorInfo) {
                errorInfo.status = 1 as Short
                errorInfo.fixTime = new Date()
                errorInfo.save flush: true
            }

            //更新状态
            def layer = VendLayerTrackGoods.findByVendMachineAndOrbitalNo(machine, orbitalNo)
            layer.workStatus = 1 as Short
            layer.sellStatus = 1 as Short
            layer.save flush: true

            //没有其它错误,则修改状态为正常。
            def errorList = VendMachineInfoErrorInfo.findAllByVendMachine(machine)
            if (errorList.size() == 0) {
                machine.status = 1 as Short
                machine.save flush: true
            }

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

    /**
     * 手机APP查询某个操作员对应的运营商旗下的所有设备情况
     * @param token
     * @return
     */
    def getNumOfMachineByToken(String token) {
        def map = [:]
        //检查token是否有效
        if (PartnerUserUtil.checkTokenValid(token)) {
            def userId = PartnerUserUtil.getPartnerUserIdFromToken(token)
            def partnerUser = PartnerUserInfo.get(userId)
            if (partnerUser) {
                //计算数量
                def machineCount = 0
                def faultCount = 0
                def replenishCount = 0
                def themeStores = ThemeStoreOfPartner.findAllByPartner(partnerUser.parnterBaseInfo)*.baseInfo
                //循环查看每家店机器数量以及是否有错,是否需要补货。
                for (ThemeStoreBaseInfo themeStore : themeStores) {
                    def machines = VendMachineInfo.findAllByThemeStoreAndIsReal(themeStore, 1 as Short)
                    //计算机器数量
                    machineCount += machines.size()
                    for (VendMachineInfo machineInfo : machines) {
                        if (machineInfo.status == 0) {
                            //计算故障数量
                            faultCount += 1
                        }
                        def c = VendLayerTrackGoods.createCriteria()
                        def results = c.list {
                            and {
                                eq("vendMachine", machineInfo)
                                gtProperty("largestInventory", "currentInventory")
                            }
                        }
                        if (results) {
                            if (results.size() > 0) {
                                replenishCount += 1
                            }
                        }
                    }
                }
                //生成结果
                map.machineCount = machineCount
                map.faultCount = faultCount
                map.replenishCount = replenishCount
                map.success = true
                map.msg = ""
            } else {
                map.success = false
                map.msg = "登录凭据己失效,请重新登录"
            }
        } else {
            map.success = false
            map.msg = "请重新登录"
        }
        render map as JSON
    }

    /**
     * 二维码扫描结果,如果存在则推送消息。
     * @param token
     * @param vmCode
     * @return
     */
    def checkQRCode(String token, String vmCode) {
        def map = [:]
        //检查token是否有效
        if (PartnerUserUtil.checkTokenValid(token)) {
            def userId = PartnerUserUtil.getPartnerUserIdFromToken(token)
            def partnerUser = PartnerUserInfo.get(userId)
            if (partnerUser) {
                def machine = VendMachineInfo.findByMachineCode(vmCode)
                if (machine) {
                    def themeStore = machine.themeStore
                    if (partnerUser.parnterBaseInfo.equals(ThemeStoreOfPartner.findByBaseInfo(themeStore).partner)) {
                        String clientId = machine.clientId
                        LoginPushVo loginVo = new LoginPushVo()
                        loginVo.type = LoginPushVo.TYPE_LOGIN_TS
                        loginVo.operatorId = userId
                        loginVo.operatorName = partnerUser.realName
                        String content = loginVo as JSON
                        //个推
                        MsgPushHelper msgPushHelper = new MsgPushHelper();
                        if (msgPushHelper.sendMsg(clientId, content)) {
                            map.success = true
                            map.msg = "指令发送成功"
                        } else {
                            map.success = false
                            map.msg = "指令发送失败"
                        }
                    } else {
                        //生成结果
                        map.success = false
                        map.msg = "这不是您管理的设备。"
                    }
                } else {
                    map.success = false
                    map.msg = "指定售卖机不存在"
                }
            } else {
                map.success = false
                map.msg = "登录凭据己失效,请重新登录"
            }
        } else {
            map.success = false
            map.msg = "请重新登录"
        }
        render map as JSON
    }

    /**
     * 确认登录设备进行管理操作。
     * @param token
     * @param vmCode
     * @return
     */
    def authQRCode(String token, String vmCode) {
        def map = [:]
        //检查token是否有效
        if (PartnerUserUtil.checkTokenValid(token)) {
            def userId = PartnerUserUtil.getPartnerUserIdFromToken(token)
            def partnerUser = PartnerUserInfo.get(userId)
            if (partnerUser) {
                def machine = VendMachineInfo.findByMachineCode(vmCode)
                if (machine) {
                    def themeStore = machine.themeStore
                    if (partnerUser.parnterBaseInfo.equals(ThemeStoreOfPartner.findByBaseInfo(themeStore).partner)) {
                        String clientId = machine.clientId
                        LoginPushVo loginVo = new LoginPushVo()
                        loginVo.type = LoginPushVo.TYPE_LOGIN
                        loginVo.operatorId = userId
                        loginVo.operatorName = partnerUser.realName
                        String content = loginVo as JSON
                        //个推
                        MsgPushHelper msgPushHelper = new MsgPushHelper();
                        if (msgPushHelper.sendMsg(clientId, content)) {
                            map.success = true
                            map.msg = "指令发送成功"
                        } else {
                            map.success = false
                            map.msg = "指令发送失败"
                        }
                    } else {
                        //生成结果
                        map.success = false
                        map.msg = "这不是您管理的设备。"
                    }
                } else {
                    map.success = false
                    map.msg = "指定售卖机不存在"
                }
            } else {
                map.success = false
                map.msg = "登录凭据己失效,请重新登录"
            }
        } else {
            map.success = false
            map.msg = "请重新登录"
        }
        render map as JSON
    }

}
