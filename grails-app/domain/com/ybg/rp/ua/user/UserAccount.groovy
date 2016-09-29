package com.ybg.rp.ua.user

import grails.databinding.BindingFormat

class UserAccount {

    static belongsTo = [user: UserBaseInfo]

    static constraints = {
    }

    Float totalMoney
    Float useableMoney
    Float usedMoney
    @BindingFormat("yyyy-MM-dd HH:mm:ss")
    Date updateTime
}
