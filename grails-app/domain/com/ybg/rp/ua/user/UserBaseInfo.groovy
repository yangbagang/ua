package com.ybg.rp.ua.user

class UserBaseInfo {

    static constraints = {
        loginName nullable: false
        password nullable: true
        openId nullable: true
        nickName nullable: true
        birthDay nullable: true
        realName nullable: true
        email nullable: true
        avator nullable: true
        sex nullable: true
        registerTime nullable: false
        registerFrom nullable: false
        isActivate nullable: true
        lastLoginTime nullable: true
        recommendId nullable: true
    }

    String loginName
    String password
    String openId
    String nickName
    Date birthDay
    String realName
    String email
    String phone
    Short sex
    String avator
    Date registerTime
    Short registerFrom
    Short isActivate
    Date lastLoginTime
    Long recommendId
}
