package com.ybg.rp.ua.analysis

import com.ybg.rp.ua.partner.PartnerUserInfo
import com.ybg.rp.ua.utils.DateUtil
import com.ybg.rp.ua.utils.PartnerUserUtil
import grails.converters.JSON

class DataAnalysisController {

    def dataAnalysisService

    /**
     * 查询得到今日销售金额及次数
     * @param token
     * @param themeIds
     * @return
     */
    def queryTodayData(String token, String themeIds) {
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

    /**
     * 得到每小时的销售数据
     * @param token
     * @param themeIds
     * @return
     */
    def queryHourData(String token, String themeIds) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def today = DateUtil.getToday()
            def moneyList = dataAnalysisService.gatherHoursMoneyNum(user.parnterBaseInfo.id, themeIds, today, today)
            def countList = dataAnalysisService.gatherHoursGoodsNum(user.parnterBaseInfo.id, themeIds, today, today)
            map.success = true
            map.moneyList = moneyList
            map.countList = countList
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 查询得到昨天与今天的销售金额对比
     * @param token
     * @param themeIds
     * @param orderBy
     * @param pageNum
     * @param pageSize
     * @return
     */
    def queryGoodsMoney(String token, String themeIds, String orderBy, Integer pageNum, Integer pageSize) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def list = dataAnalysisService.queryGoodsMoney(user.parnterBaseInfo.id, themeIds, today, yesterday, orderBy, pageNum, pageSize)
            map.success = true
            map.dataList = list
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 查询得到昨天与今天的销售数量对比
     * @param token
     * @param themeIds
     * @param orderBy
     * @param pageNum
     * @param pageSize
     * @return
     */
    def queryGoodsCount(String token, String themeIds, String orderBy, Integer pageNum, Integer pageSize) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询总共有多少钱
            def today = DateUtil.getToday()
            def yesterday = DateUtil.getYesterday()
            def list = dataAnalysisService.queryGoodsCount(user.parnterBaseInfo.id, themeIds, today, yesterday, orderBy, pageNum, pageSize)
            map.success = true
            map.dataList = list
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 对比昨天与前天的数据
     * @param token
     * @param themeIds
     * @return
     */
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

    /**
     * 商品销售排行
     * @param token
     * @param themeIds
     * @param orderBy 排序依据,count按销售数量降序。money按金额降序。
     * @param fromDate
     * @param toDate
     * @param pageNum
     * @param pageSize
     * @return
     */
    def queryGoods(String token, String themeIds, String orderBy, String fromDate, String toDate, Integer pageNum, Integer pageSize) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //查询
            def list = dataAnalysisService.queryGoods(user.parnterBaseInfo.id, themeIds, fromDate, toDate, orderBy, pageNum, pageSize)
            map.success = true
            map.dataList = list
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

    /**
     * 商品分析
     * @param token
     * @param themeIds
     * @param fromDate
     * @param toDate
     * @return
     */
    def goodsAnalysis(String token, String themeIds, String fromDate, String toDate) {
        def map = [:]
        if (PartnerUserUtil.checkTokenValid(token)){
            def user = PartnerUserInfo.get(PartnerUserUtil.getPartnerUserIdFromToken(token))
            //总金额
            def totalMoney = dataAnalysisService.gatherMoneyNum(user.parnterBaseInfo.id, themeIds, fromDate, toDate)
            //总件数
            def totalCount = dataAnalysisService.gatherGoodsNum(user.parnterBaseInfo.id, themeIds, fromDate, toDate)
            //金额前3名
            def moneyList = dataAnalysisService.queryGoods(user.parnterBaseInfo.id, themeIds, fromDate, toDate, "money", 1, 3)
            //件数前3名
            def countList = dataAnalysisService.queryGoods(user.parnterBaseInfo.id, themeIds, fromDate, toDate, "count", 1, 3)
            //构造结果
            map.success = true
            map.totalMoney = totalMoney
            map.totalCount = totalCount
            map.moneyList = moneyList
            map.countList = countList
            map.message = ""
        } else {
            map.success = false
            map.message = "为了您的账号安全，请重新登陆"
        }
        render map as JSON
    }

}
