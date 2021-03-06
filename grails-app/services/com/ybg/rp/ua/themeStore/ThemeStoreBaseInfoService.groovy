package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.partner.PartnerUserInfo
import com.ybg.rp.ua.partner.PartnerUserStore
import com.ybg.rp.ua.utils.GPSUtil
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.transaction.Transactional

@Transactional
class ThemeStoreBaseInfoService {

    def findThemeStore(String token, String name) {
        def map = [:]
        def userId = PartnerUserUtil.getPartnerUserIdFromToken(token)
        def partnerUser = PartnerUserInfo.get(userId)
        if (partnerUser) {
            //计算数量
            def themeStores = PartnerUserStore.findAllByUser(partnerUser)*.store
            def voList = createVoList(themeStores, name, 0, 0)
            voList?.sort {it.name}
            //生成结果
            map.dataList = voList
            map.totalCount = voList?.size()
            map.success = true
            map.msg = ""
        } else {
            map.success = false
            map.msg = "登录凭据己失效,请重新登录。"
        }
        map
    }

    def findThemeStore2(String name, Double latitude, Double longitude) {
        def map = [:]
        //计算数量
        def themeStores = getStores(name)
        def voList = createVoList(themeStores, name, latitude, longitude)
        voList?.sort {it.distance}
        //生成结果
        map.dataList = voList
        map.totalCount = voList?.size()
        map.success = true
        map.msg = ""
        map
    }

    private createVoList(List<ThemeStoreBaseInfo> themeStoreBaseInfoList, String name, Double latitude, Double longitude) {
        def list = []
        for (ThemeStoreBaseInfo storeBaseInfo : themeStoreBaseInfoList) {
            if (name == null || "" == name || storeBaseInfo.name.contains(name)) {
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

    def getStores(String name) {
        def stores = []
        if (name) {
            stores = ThemeStoreBaseInfo.findAllByStatusAndNameLike(1 as Short, "%${name}%")
        } else {
            stores = ThemeStoreBaseInfo.findAllByStatus(1 as Short)
        }
        stores
    }
}
