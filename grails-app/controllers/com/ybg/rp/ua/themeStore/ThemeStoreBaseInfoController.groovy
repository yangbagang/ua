package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON

class ThemeStoreBaseInfoController {

    def themeStoreBaseInfoService

    /**
     * 列出某合作伙伴经营的所有主题店。
     * @param token
     * @param name
     * @param latitude
     * @param longitude
     * @return
     */
    def listThemeStore(String token, String name) {
        def map = [:]
        //检查token是否有效
        if (PartnerUserUtil.checkTokenValid(token)) {
            if (name == null) {
                name = ""
            }
            map = themeStoreBaseInfoService.findThemeStore(token, name)
        } else {
            map.success = false
            map.msg = "登录凭据己失效,请重新登录。"
        }

        render map as JSON
    }

    /**
     * 列出正在营业的主题店。for weixin
     * @param name
     */
    def listStores(String name) {
        def stores = themeStoreBaseInfoService.getStores(name)
        render(view: "list", model: [stores: stores])
    }
}
