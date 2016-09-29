package com.ybg.rp.ua.user

class UserAddressInfo {

    static belongsTo = [userInfo: UserBaseInfo]

    static constraints = {

    }

    String province
    String city
    String area
    String phoneNo
    String name
    String address
    Short isDefault
}
