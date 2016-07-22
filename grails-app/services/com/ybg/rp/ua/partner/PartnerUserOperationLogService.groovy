package com.ybg.rp.ua.partner

import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.transaction.Transactional

@Transactional
class PartnerUserOperationLogService {

    def addLog(String token, Short type, Long deviceId, String orbitalNo, Integer num, Short result) {
        def log = new PartnerUserOperationLog()
        log.user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
        log.operationTime = new Date()
        log.operationType = type
        log.operationDesc = ""
        log.deviceId = deviceId
        log.orbitalNo = orbitalNo
        log.targetId = deviceId
        log.targetNum = num
        log.result = result
        log.save flush: true
    }
}
