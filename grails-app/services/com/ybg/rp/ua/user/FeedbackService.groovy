package com.ybg.rp.ua.user

import com.ybg.rp.ua.partner.PartnerUserInfo
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.transaction.Transactional

@Transactional
class FeedbackService {

    @Transactional(readOnly = true)
    def list(Short flag, Integer pageNum, Integer pageSize) {
        def c = Feedback.createCriteria()
        def result = c.list(max: pageSize, offset: (pageNum - 1) * pageSize) {
            if (flag == 0 || flag == 1) {
                eq("flag", flag)
            }
            order("createTime", "desc")
        }
        result
    }

    def update(String token, Long feedbackId, String content) {
        def userId = PartnerUserUtil.getPartnerUserIdFromToken(token)
        def feedback = Feedback.get(feedbackId)
        if (feedback) {
            feedback.updateTime = new Date()
            feedback.flag = 1 as Short
            feedback.result = content
            feedback.partnerUserId = userId
            feedback.save(flush: true)
        }
    }
}
