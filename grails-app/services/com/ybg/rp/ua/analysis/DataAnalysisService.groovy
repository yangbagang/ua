package com.ybg.rp.ua.analysis

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional(readOnly = true)
class DataAnalysisService {

    def dataSource

    def gatherMoneyNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select sum(goods_num*buy_price) as money from data_analysis " +
                "where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        def money = sql.firstRow(query).money
        money ? money.round(2) : 0
    }

    def gatherOrderNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select count(distinct order_no) as orderNum from data_analysis " +
                "where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        def orderNum = sql.firstRow(query).orderNum
        orderNum
    }

    def gatherOnlineMoneyNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select sum(goods_num*buy_price) as money from data_analysis " +
                "where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        query += " and pay_way > 0"
        def money = sql.firstRow(query).money
        money ? money.round(2) : 0
    }

    def gatherOnlineOrderNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select count(distinct order_no) as orderNum from data_analysis " +
                "where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        query += " and pay_way > 0"
        def orderNum = sql.firstRow(query).orderNum
        orderNum
    }

    def gatherHoursMoneyNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select complete_hour,buy_price,goods_num from data_analysis " +
                "where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        def money1 = 0
        def money2 = 0
        def money3 = 0
        def money4 = 0
        sql.eachRow(query) { row ->
            if (row.complete_hour <= 6) {
                money1 += row.buy_price * row.goods_num
            } else if (row.complete_hour <= 12) {
                money2 += row.buy_price * row.goods_num
            } else if (row.complete_hour <= 18) {
                money3 += row.buy_price * row.goods_num
            } else {
                money4 += row.buy_price * row.goods_num
            }
        }

        def list = [["timeSlot": 1, "money": money1],
                     ["timeSlot": 2, "money": money2],
                     ["timeSlot": 3, "money": money3],
                     ["timeSlot": 4, "money": money4]]
        list
    }

    def gatherHoursOrderNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select complete_hour as completeHour,count(distinct order_no) as num" +
                " from data_analysis where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        query += " group by order_no"
        def count1 = 0
        def count2 = 0
        def count3 = 0
        def count4 = 0
        sql.eachRow(query) { row ->
            if (row.completeHour <= 6) {
                count1 += row.num
            } else if (row.completeHour <= 12) {
                count2 += row.num
            } else if (row.completeHour <= 18) {
                count3 += row.num
            } else {
                count4 += row.num
            }
        }
        def list = [["timeSlot": 1, "money": count1],
                     ["timeSlot": 2, "money": count2],
                     ["timeSlot": 3, "money": count3],
                     ["timeSlot": 4, "money": count4]]
        list
    }

    def queryGoodsMoney(Long partnerId, String themeIds, String today, String yesterday, String orderBy, Integer pageNum, Integer pageSize) {
        def sql = new Sql(dataSource)
        def query = """
                    select a.`goods_pic` as goodsPic,a.`theme_store_goods_id` as gid,a.`goods_name` as goodsName,
                    b.money as transMoney1, c.money as transMoney2
                    from data_analysis a
                    left join
                    (select theme_store_goods_id as goodsId, sum(buy_price*goods_num) as money
                    from data_analysis where date(complete_time)='${today}' group by theme_store_goods_id) b
                    on a.theme_store_goods_id = b.goodsId
                    left join
                    (select theme_store_goods_id as goodsId, sum(buy_price*goods_num) as money
                    from data_analysis where date(complete_time)='${yesterday}' group by theme_store_goods_id) c
                    on a.theme_store_goods_id = c.goodsId
                    where 1=1
                    """
        if (themeIds != null && themeIds != "") {
            query += " and a.theme_store_id in (${themeIds})"
        }
        if (partnerId != null &&  partnerId != 0) {
            query += " and a.partner_id = ${partnerId}"
        }
        if (orderBy != "desc") {
            query += " order by b.money desc"
        } else {
            query += " order by b.money asc"
        }
        sql.rows(query, (pageNum - 1) * pageSize, pageSize)
    }

    def queryGoodsCount(Long partnerId, String themeIds, String today, String yesterday, String orderBy, Integer pageNum, Integer pageSize) {
        def sql = new Sql(dataSource)
        def query = """
                    select a.`goods_pic` as goodsPic,a.`theme_store_goods_id` as gid,a.`goods_name` as goodsName,
                    b.transCn as transCn1, c.transCn as transCn2
                    from data_analysis a
                    left join
                    (select theme_store_goods_id as goodsId, sum(goods_num) as transCn
                    from data_analysis where date(complete_time)='${today}' group by theme_store_goods_id) b
                    on a.theme_store_goods_id = b.goodsId
                    left join
                    (select theme_store_goods_id as goodsId, sum(goods_num) as transCn
                    from data_analysis where date(complete_time)='${yesterday}' group by theme_store_goods_id) c
                    on a.theme_store_goods_id = c.goodsId
                    where 1=1
                    """
        if (themeIds != null && themeIds != "") {
            query += " and a.theme_store_id in (${themeIds})"
        }
        if (partnerId != null &&  partnerId != 0) {
            query += " and a.partner_id = ${partnerId}"
        }
        if (orderBy != "asc") {
            query += " order by b.transCn desc"
        } else {
            query += " order by b.transCn asc"
        }
        sql.rows(query, (pageNum - 1) * pageSize, pageSize)
    }

}
