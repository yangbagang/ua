package com.ybg.rp.ua.partner

import com.ybg.rp.ua.themeStore.ThemeStoreBaseInfo
import grails.gorm.DetachedCriteria

class PartnerUserStore {

    static belongsTo = [user: PartnerUserInfo, store: ThemeStoreBaseInfo]

    static constraints = {
    }

}
