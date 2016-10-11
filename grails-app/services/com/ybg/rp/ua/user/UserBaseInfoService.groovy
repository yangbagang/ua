package com.ybg.rp.ua.user

import grails.transaction.Transactional

@Transactional
class UserBaseInfoService {

    def checkOpenId(String openId) {
        if (openId) {
            def userBaseInfo = UserBaseInfo.findByOpenId(openId)
            if (!userBaseInfo) {
                userBaseInfo = new UserBaseInfo()
                userBaseInfo.openId = openId
                userBaseInfo.loginName = ""
                userBaseInfo.phone = ""
                userBaseInfo.registerTime = new Date()
                userBaseInfo.registerFrom = 1 as Short
                userBaseInfo.save flush: true
            }
        }
    }

    def checkPhone(String openId, String phone) {
        def userBaseInfo = UserBaseInfo.findByOpenId(openId)
        if (userBaseInfo) {
            userBaseInfo.phone = phone
            userBaseInfo.registerTime = new Date()
            userBaseInfo.registerFrom = 1 as Short
            userBaseInfo.save flush: true
            return true
        }
        return false
    }

}
