package com.ybg.rp.ua.analysis

import com.ybg.rp.ua.partner.PartnerBaseInfo
import com.ybg.rp.ua.themeStore.ThemeStoreBaseInfo
import com.ybg.rp.ua.themeStore.ThemeStoreOfPartner
import com.ybg.rp.ua.utils.DateUtil
import com.ybg.rp.ua.utils.NumberUtil
import grails.transaction.Transactional

@Transactional
class DataAnalysisService {

    def countMoney(PartnerBaseInfo partner, String themeIds, String fromDay, String toDay) {
        def idList = getIdList(partner, themeIds)
        def c = DataAnalysis.createCriteria()
        def money = c.get {
            projections {
                sqlProjection 'sum(buy_price * goods_num) as money', 'money', FLOAT
            }
            and {
                'in'("themeStoreId", idList)
                gt("completeTime", DateUtil.getFromTime(fromDay))
                lt("completeTime", DateUtil.getToTime(toDay))
            }
        }
        money
    }

    def countOrderNum(PartnerBaseInfo partner, String themeIds, String fromDay, String toDay) {
        def idList = getIdList(partner, themeIds)
        def c = DataAnalysis.createCriteria()
        def count = c.get {
            projections {
                countDistinct("orderNo")
            }
            and {
                'in'("themeStoreId", idList)
                gt("completeTime", DateUtil.getFromTime(fromDay))
                lt("completeTime", DateUtil.getToTime(toDay))
            }
        }
        count
    }

    def getIdList(PartnerBaseInfo partner, String themeIds) {
        if (themeIds != null && themeIds != "" && themeIds != "0") {
            return NumberUtil.getListFromString(themeIds)
        } else {
            def idList = []
            def stores = ThemeStoreOfPartner.findAllByPartner(partner)*.baseInfo
            for (ThemeStoreBaseInfo info: stores) {
                idList.add(info.id)
            }
            return idList
        }
    }

    def countOnlineMoney(PartnerBaseInfo partner, String themeIds, String fromDay, String toDay) {
        def idList = getIdList(partner, themeIds)
        def c = DataAnalysis.createCriteria()
        def money = c.get {
            projections {
                sqlProjection 'sum(buy_price * goods_num) as money', 'money', FLOAT
            }
            and {
                'in'("themeStoreId", idList)
                gt("completeTime", DateUtil.getFromTime(fromDay))
                lt("completeTime", DateUtil.getToTime(toDay))
                gt("payWay", 0 as Short)
            }
        }
        money
    }

    def countOnlineOrderNum(PartnerBaseInfo partner, String themeIds, String fromDay, String toDay) {
        def idList = getIdList(partner, themeIds)
        def c = DataAnalysis.createCriteria()
        def count = c.get {
            projections {
                countDistinct("orderNo")
            }
            and {
                'in'("themeStoreId", idList)
                gt("completeTime", DateUtil.getFromTime(fromDay))
                lt("completeTime", DateUtil.getToTime(toDay))
                gt("payWay", 0 as Short)
            }
        }
        count
    }

    def countHoursMoney(PartnerBaseInfo partner, String themeIds, String fromDay, String toDay) {
        def idList = getIdList(partner, themeIds)
        def c = DataAnalysis.createCriteria()
        def result = c.list {
            and {
                'in'("themeStoreId", idList)
                gt("completeTime", DateUtil.getFromTime(fromDay))
                lt("completeTime", DateUtil.getToTime(toDay))
            }
        }
        def money1 = 0
        def money2 = 0
        def money3 = 0
        def money4 = 0
        for (DataAnalysis analysis: result) {
            if (analysis.completeHour <= 6) {
                money1 += analysis.buyPrice * analysis.goodsNum
            } else if (analysis.completeHour <= 12) {
                money2 += analysis.buyPrice * analysis.goodsNum
            } else if (analysis.completeHour <= 18) {
                money3 += analysis.buyPrice * analysis.goodsNum
            } else {
                money4 += analysis.buyPrice * analysis.goodsNum
            }
        }
        def list = [["timeSlot": 1, "money": money1],
                     ["timeSlot": 2, "money": money2],
                     ["timeSlot": 3, "money": money3],
                     ["timeSlot": 4, "money": money4]]
        list
    }

    def countHoursOrderNum(PartnerBaseInfo partner, String themeIds, String fromDay, String toDay) {
        def idList = getIdList(partner, themeIds)
        def c = DataAnalysis.createCriteria()
        def result = c.list {
            and {
                'in'("themeStoreId", idList)
                gt("completeTime", DateUtil.getFromTime(fromDay))
                lt("completeTime", DateUtil.getToTime(toDay))
            }
        }
        def count1 = 0
        def count2 = 0
        def count3 = 0
        def count4 = 0
        Set<String> set = new HashSet<String>()
        for (DataAnalysis analysis: result) {
            if (set.contains(analysis.orderNo)) {
                continue
            }
            set.add(analysis.orderNo)
            if (analysis.completeHour <= 6) {
                count1 += 1
            } else if (analysis.completeHour <= 12) {
                count2 += 1
            } else if (analysis.completeHour <= 18) {
                count3 += 1
            } else {
                count4 += 1
            }
        }
        def list = [["timeSlot": 1, "money": count1],
                     ["timeSlot": 2, "money": count2],
                     ["timeSlot": 3, "money": count3],
                     ["timeSlot": 4, "money": count4]]
        list
    }
}
