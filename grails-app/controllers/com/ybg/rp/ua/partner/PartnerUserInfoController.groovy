package com.ybg.rp.ua.partner

import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder

class PartnerUserInfoController {

    /**
     * 手机管理APP登录。用户名与密码获配时返回token。
     * @param userName
     * @param password
     * @param loginDevice
     * @return
     */
    def login(String userName, String password, String loginDevice) {
        def map = [:]
        def userInfo = PartnerUserInfo.findByUsernameAndEnabled(userName, true)
        if (userInfo) {
            def crypto = new BCryptPasswordEncoder(
                    (int) SpringSecurityUtils.securityConfig.password.bcrypt.logrounds)
            if (crypto.isPasswordValid(userInfo.password, password, null)) {
                //生成token
                def token = PartnerUserUtil.createUserTokenFromId(userInfo.id)
                //生成记录
                def loginRecord = new PartnerUserLoginRecord()
                loginRecord.userInfo = userInfo
                loginRecord.loginTime = new Date()
                loginRecord.loginDevice = loginDevice
                loginRecord.loginIp = request.remoteAddr
                loginRecord.token = token
                loginRecord.save flush: true
                //生成结果
                map.success = true
                map.token = token
                map.msg = "登录成功。"
                map.userInfo = userInfo
            } else {
                map.success = false
                map.msg = "用户名或密码错误。"
            }
        } else {
            map.success = false
            map.msg = "用户名或密码错误。"
        }
        render map as JSON
    }

    /**
     * 退出系统, 移除token。
     * @param token
     * @return
     */
    def logout(String token) {
        def map = [:]
        if (token) {
            if (PartnerUserUtil.removeToken(token)) {
                map.success = true
                map.msg = "退出成功。"
            } else {
                map.success = false
                map.msg = "退出失败。"
            }
        } else {
            map.success = false
            map.msg = "参数不能为空。"
        }
        render map as JSON
    }

}
