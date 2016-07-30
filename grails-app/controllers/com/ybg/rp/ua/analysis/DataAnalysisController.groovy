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
            def today = DateUtil.getToday()
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def totalMoney = dataAnalysisService.gatherMoneyNum(user.parnterBaseInfo.id, themeIds, today, today)
            //查询总共有几笔订单
            def totalCount = dataAnalysisService.gatherOrderNum(user.parnterBaseInfo.id, themeIds, today, today)
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
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def todayMoney = dataAnalysisService.gatherMoneyNum(user.parnterBaseInfo.id, themeIds, today, today)
            def yestMoney = dataAnalysisService.gatherMoneyNum(user.parnterBaseInfo.id, themeIds, yesterday, yesterday)
            def onlineMoney = dataAnalysisService.gatherOnlineMoneyNum(user.parnterBaseInfo.id, themeIds, today, today)
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
        render map as JSON
    }

    def queryCount(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有几笔订单
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def todayCount = dataAnalysisService.gatherOrderNum(user.parnterBaseInfo.id, themeIds, today, today)
            def yestCount = dataAnalysisService.gatherOrderNum(user.parnterBaseInfo.id, themeIds, yesterday, yesterday)
            def onlineCount = dataAnalysisService.gatherOnlineOrderNum(user.parnterBaseInfo.id, themeIds, today, today)
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
        render map as JSON
    }

    def queryLineMoney(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def todayMoney = dataAnalysisService.gatherHoursMoneyNum(user.parnterBaseInfo.id, themeIds, today, today)
            def yesterdayMoney = dataAnalysisService.gatherHoursMoneyNum(user.parnterBaseInfo.id, themeIds, yesterday, yesterday)
            map.success = true
            map.todayMoney = todayMoney
            map.yesterdayMoney = yesterdayMoney
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    def queryLineCount(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def todayCount = dataAnalysisService.gatherHoursOrderNum(user.parnterBaseInfo.id, themeIds, today, today)
            def yesterdayCount = dataAnalysisService.gatherHoursOrderNum(user.parnterBaseInfo.id, themeIds, yesterday, yesterday)
            map.success = true
            map.todayCount = todayCount
            map.yesterdayCount = yesterdayCount
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    def queryGoodsMoney(String token, String themeIds, String orderBy, Integer pageNum, Integer pageSize) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def list = dataAnalysisService.queryGoodsMoney(user.parnterBaseInfo.id, themeIds, today, yesterday, orderBy, pageNum, pageSize)
            map.success = false
            map.dataList = list
            map.currPage = pageNum
            map.totalPage = 10000//有多少页没有意义
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    def queryGoodsCount(String token, String themeIds, String orderBy, Integer pageNum, Integer pageSize) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def list = dataAnalysisService.queryGoodsCount(user.parnterBaseInfo.id, themeIds, today, yesterday, orderBy, pageNum, pageSize)
            map.success = false
            map.dataList = list
            map.currPage = pageNum
            map.totalPage = 10000//有多少页没有意义
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    def queryCompareData(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)) {
            def zt = DateUtil.getBeforeDay(1)//昨天
            def qt = DateUtil.getBeforeDay(2)//前天
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def ztMoney = dataAnalysisService.gatherMoneyNum(user.parnterBaseInfo.id, themeIds, zt, zt)
            def qtMoney = dataAnalysisService.gatherMoneyNum(user.parnterBaseInfo.id, themeIds, qt, qt)
            //查询总共有几笔订单
            def ztCount = dataAnalysisService.gatherOrderNum(user.parnterBaseInfo.id, themeIds, zt, zt)
            def qtCount = dataAnalysisService.gatherOrderNum(user.parnterBaseInfo.id, themeIds, qt, qt)
            //数据变化
            def moneyCompare = 0
            if (qtMoney != 0) {
                moneyCompare = (int)((ztMoney - qtMoney) * 100 / qtMoney)
            }
            def countCompare = 0
            if (qtCount != 0) {
                countCompare = (int)((ztCount - qtCount) * 100 / qtCount)
            }
            map.success = true
            map.ztMoney = ztMoney
            map.qtMoney = qtMoney
            map.ztCount = ztCount
            map.qtCount = qtCount
            map.moneyCompare = moneyCompare
            map.countCompare = countCompare
            map.zt = zt
            map.qt = qt
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }
}
