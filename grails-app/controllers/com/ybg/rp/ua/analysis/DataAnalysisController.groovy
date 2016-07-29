package com.ybg.rp.ua.analysis

import com.ybg.rp.ua.partner.PartnerUserInfo
import com.ybg.rp.ua.utils.DateUtil
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON

class DataAnalysisController {

    def dataAnalysisService

    def queryTotalData(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            if (themeIds == null || themeIds == "") {
                themeIds = "0"
            }
            def today = DateUtil.getToday()
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def totalMoney = dataAnalysisService.countMoney(user.parnterBaseInfo, themeIds, today, today)
            //查询总共有几笔订单
            def totalCount = dataAnalysisService.countOrderNum(user.parnterBaseInfo, themeIds, today, today)
            map.totalCount = totalCount
            map.totalMoney = totalMoney
            map.success = true
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    def queryMoney(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            if (themeIds == null || themeIds == "") {
                themeIds = "0"
            }
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def todayMoney = dataAnalysisService.countMoney(user.parnterBaseInfo, themeIds, today, today)
            def yestMoney = dataAnalysisService.countMoney(user.parnterBaseInfo, themeIds, yesterday, yesterday)
            def onlineMoney = dataAnalysisService.countOnlineMoney(user.parnterBaseInfo, themeIds, today, today)
            def online = 0
            if (todayMoney != 0) {
                online = (int) (onlineMoney * 100 / todayMoney)
            }
            map.success = true
            map.todayMoney = todayMoney
            map.yestMoney = yestMoney
            map.online1 = online
            map.online2 = 100 - online
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
    }

    def queryCount(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            if (themeIds == null || themeIds == "") {
                themeIds = "0"
            }
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有几笔订单
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def todayCount = dataAnalysisService.countOrderNum(user.parnterBaseInfo, themeIds, today, today)
            def yestCount = dataAnalysisService.countOrderNum(user.parnterBaseInfo, themeIds, yesterday, yesterday)
            def onlineCount = dataAnalysisService.countOnlineOrderNum(user.parnterBaseInfo, themeIds, today, today)
            def online = 0
            if (todayCount != 0) {
                online = (int) (onlineCount * 100 / todayCount)
            }
            map.success = true
            map.todayCount = todayCount
            map.yestCount = yestCount
            map.online1 = online
            map.online2 = 100 - online
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
    }

    def queryLineMoney(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            if (themeIds == null || themeIds == "") {
                themeIds = "0"
            }
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def todayMoney = dataAnalysisService.countHoursMoney(user.parnterBaseInfo, themeIds, today, today)
            def yesterdayMoney = dataAnalysisService.countHoursMoney(user.parnterBaseInfo, themeIds, yesterday, yesterday)
            map.success = true
            map.todayMoney = todayMoney
            map.yesterdayMoney = yesterdayMoney
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
    }

    def queryLineCount(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            if (themeIds == null || themeIds == "") {
                themeIds = "0"
            }
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def todayCount = dataAnalysisService.countHoursOrderNum(user.parnterBaseInfo, themeIds, today, today)
            def yesterdayCount = dataAnalysisService.countHoursOrderNum(user.parnterBaseInfo, themeIds, yesterday, yesterday)
            map.success = true
            map.todayCount = todayCount
            map.yesterdayCount = yesterdayCount
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
    }

    def queryGoodsMoney(String token, String themeIds, String orderBy,Integer currentPage) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            if (themeIds == null || themeIds == "") {
                themeIds = "0"
            }
//            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
//            //查询总共有多少钱
//            def today = DateUtil.getToday()
//            def yesterday = DateUtil.getYesterday()
//            def todayMoney = dataAnalysisService.countHoursMoney(user.parnterBaseInfo, themeIds, today, today)
//            def yesterdayMoney = dataAnalysisService.countHoursMoney(user.parnterBaseInfo, themeIds, yesterday, yesterday)
            map.success = false
            //map.put("dataList",list.getData());
            //map.put("currPage",list.getCurrentPageNo());
            //map.put("totalPage",list.getTotalPageCount());
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
    }

    def queryGoodsCount(String token, String themeIds, String orderBy,Integer currentPage) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            if (themeIds == null || themeIds == "") {
                themeIds = "0"
            }
//            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
//            //查询总共有多少钱
//            def today = DateUtil.getToday()
//            def yesterday = DateUtil.getYesterday()
//            def todayCount = dataAnalysisService.countHoursOrderNum(user.parnterBaseInfo, themeIds, today, today)
//            def yesterdayCount = dataAnalysisService.countHoursOrderNum(user.parnterBaseInfo, themeIds, yesterday, yesterday)
            map.success = false
            //map.todayCount = todayCount
            //map.yesterdayCount = yesterdayCount
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
    }

}
