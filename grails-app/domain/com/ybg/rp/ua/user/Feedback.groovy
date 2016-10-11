package com.ybg.rp.ua.user

import com.ybg.rp.ua.partner.PartnerUserInfo

class Feedback {

    static constraints = {
        openid nullable: true
        userid nullable: true
        userName nullable: true
        createTime nullable: true
        vmCode nullable: true
        type nullable: true
        content nullable: true
        partnerUserId nullable: true
        updateTime nullable: true
        result nullable: true
        flag nullable: true
    }

    String openid//微信ID
    Long userid//用户ID
    String userName//用户名
    Date createTime//提交时间
    String vmCode//机器编号
    String type//类型
    String content//具体描述
    Long partnerUserId//处理者
    Date updateTime//处理时间
    String result//具体处理方式
    Short flag=0//标志位，是否处理。0未处理1己处理

    transient String partnerUserName

    String getPartnerUserName() {
        PartnerUserInfo.get(partnerUserId)?.realName
    }

}
