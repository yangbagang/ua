package com.ybg.rp.ua.user

import com.ybg.rp.ua.partner.PartnerUserInfo
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON

class FeedbackController {

    def feedbackService

    /**
     * 微信新建反馈
     */
    def create() {

    }

    /**
     * 保存反馈
     * @param feedback
     * @return
     */
    def save(Feedback feedback) {
        if (feedback) {
            feedback.createTime = new Date()
            feedback.save flush: true
        }
        render(view: "save")
    }

    /**
     * 显示反馈详情
     * @param feedback
     * @return
     */
    def show(Feedback feedback) {
        respond feedback
    }

    /**
     * 列出某个人提交的反馈
     * @param openid
     * @return
     */
    def list(String openid) {
        respond Feedback.findAllByOpenid(openid)
    }

    /**
     * 显示所有反馈，分页，按条件显示。
     * @param token
     * @param flag 只显示己处理或未处理或全部
     * @param pageNum
     * @param pageSize
     */
    def listAll(String token, Short flag, Integer pageNum, Integer pageSize) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def dataList = feedbackService.list(flag, pageNum, pageSize)
            //构造结果
            map.success = true
            map.dataList = dataList
            map.message = ""
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 显示反馈详情
     * @param token
     * @param feedbackId
     * @return
     */
    def showDetail(String token, Long feedbackId) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def feedback = Feedback.get(feedbackId)
            //构造结果
            map.success = true
            map.feedback = feedback
            map.message = ""
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 处理反馈
     * @param token
     * @param feedbackId
     * @param content
     * @return
     */
    def saveContent(String token, Long feedbackId, String content) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            if (content) {
                feedbackService.update(token, feedbackId, content)
                //构造结果
                map.success = true
                map.message = ""
            } else {
                map.success = false
                map.message = "处理方式不能为空。"
            }
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }
}
