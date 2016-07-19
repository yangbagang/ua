package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.partner.PartnerUserInfo
import com.ybg.rp.ua.utils.GPSUtil
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.transaction.Transactional

@Transactional
class ThemeStoreBaseInfoService {

    def findThemeStoreByToken(String token, String name, Double latitude, Double longitude) {
        def map = [:]
        def userId = PartnerUserUtil.getPartnerUserIdFromToken(token)
        def partnerUser = PartnerUserInfo.get(userId)
        if (partnerUser) {
            //计算数量
            def themeStores = ThemeStoreOfPartner.findAllByPartner(partnerUser.parnterBaseInfo)*.baseInfo
            def voList = createVoList(themeStores, name, latitude, longitude)
            voList?.sort {it.distance}
            //生成结果
            if (voList && voList.size() > 0) {
                map.nearestList = voList[0]
            }
            if (voList && voList.size() > 1) {
                map.dataList = voList.subList(1, voList.size())
            }
            map.totalCount = voList?.size()
            map.success = true
            map.msg = ""
        } else {
            map.success = false
            map.msg = "登录凭据己失效,请重新登录。"
        }
        map
    }

    private createVoList(List<ThemeStoreBaseInfo> themeStoreBaseInfos, String name, Double latitude, Double longitude) {
        def list = []
        for (ThemeStoreBaseInfo storeBaseInfo : themeStoreBaseInfos) {
            if (name == null || "".equals(name) || storeBaseInfo.name.contains(name)) {
                def storeVo = new ThemeStoreVo()
                storeVo.id = storeBaseInfo.id
                storeVo.name = storeBaseInfo.name
                storeVo.address = storeBaseInfo.building.address
                storeVo.distance = GPSUtil.getDistance(latitude, longitude, storeBaseInfo.latitude, storeBaseInfo.longitude)
                list.add(storeVo)
            }
        }
        list
    }
}
