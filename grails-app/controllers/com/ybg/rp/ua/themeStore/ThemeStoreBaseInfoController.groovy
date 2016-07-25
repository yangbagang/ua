package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON

class ThemeStoreBaseInfoController {

    def themeStoreBaseInfoService

    /**
     * 列出某合作伙伴经营的所有主题店。按距离远近排序。
     * @param token
     * @param name
     * @param latitude
     * @param longitude
     * @return
     */
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
        render map as JSON
    }

}
