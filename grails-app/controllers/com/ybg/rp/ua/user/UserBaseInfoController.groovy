package com.ybg.rp.ua.user

import grails.converters.JSON

class UserBaseInfoController {

    def userBaseInfoService

    def editWXPhone(String openid) {
        //检查openid
        userBaseInfoService.checkOpenId(openid)
        //转到手机号页面
    }

    def checkWXPhone(String phone, String openid) {
        def viewName = "editWXPhone"
        if (!phone) {
            flash.message = "绑定失败，手机号码不能为空。"
        } else if (phone.length() != 11) {
            flash.message = "绑定失败，手机号码位数不正确。"
        } else if (userBaseInfoService.checkPhone(openid, phone)) {
            viewName = "phoneUpdate"
        } else {
            flash.message = "绑定失败，请检查号码是否正确。"
        }
        render(view: viewName)
    }

    def agreement() {

    }

    def view() {
        render(view: "phoneUpdate")
    }

    /**
     * 获取微信openid
     * @param code
     * @return
     */
    def getOpenId(String code) {
        def APPID = ""
        def SECRET = ""
        def url = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+"&secret="+SECRET+"&js_code="+code+"&grant_type=authorization_code"
        def map = url.toURL().text
        render map as JSON
    }
}
