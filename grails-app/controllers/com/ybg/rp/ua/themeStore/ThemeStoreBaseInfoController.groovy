package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON

class ThemeStoreBaseInfoController {

    def themeStoreBaseInfoService

    def listThemeStoreByToken(String token, String name, Double latitude, Double longitude) {
        def map = [:]
        //检查token是否有效
        if (PartnerUserUtil.checkTokenValid(token)) {
            if (name == null) {
                name = ""
            }
            map = themeStoreBaseInfoService.findThemeStoreByToken(token, name, latitude, longitude)
        } else {
            map.success = false
            map.msg = "登录凭据己失效,请重新登录。"
        }
        response.setHeader("Access-Control-Allow-Origin", "*")
        render map as JSON
    }

}
