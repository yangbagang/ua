package com.ybg.rp.ua.partner

class PartnerUserLoginRecord {

    static constraints = {
    }

    PartnerUserInfo userInfo
    Date loginTime
    String loginDevice
    String loginIp
    String token
}
