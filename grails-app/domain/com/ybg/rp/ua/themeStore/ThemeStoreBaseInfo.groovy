package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.partner.BuildingBaseInfo
import grails.databinding.BindingFormat

class ThemeStoreBaseInfo {

    static belongsTo = [building: BuildingBaseInfo]

    static constraints = {
        name(nullable: true)
        status(nullable: true)
        createTime(nullable: true)
        openTime(nullable: true)
        longitude(nullable: true)
        latitude(nullable: true)
        position(nullable: true)
        province(nullable: true)
        city(nullable: true)
        county(nullable: true)
    }

    String name
    Short status
    Date createTime
    @BindingFormat("yyyy-MM-dd")
    Date openTime
    Double longitude//经度
    Double latitude
    String position
    String province
    String city
    String county

    transient String partnerName
    transient Float scale

}
