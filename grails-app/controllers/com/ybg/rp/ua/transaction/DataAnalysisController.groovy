package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.partner.PartnerUserInfo
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON

class DataAnalysisController {

    def orderInfoService

    def queryTotalData(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def totalMoney = orderInfoService.countMoney(user.parnterBaseInfo, themeIds, null, null)
            //查询总共有几笔订单
            def totalCount = orderInfoService.countOrderNum(user.parnterBaseInfo, themeIds, null, null)
            map.totalCount = totalCount
            map.totalMoney = totalMoney
            map.success = true
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }
}
