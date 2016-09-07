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
        orderNum ?: 0
    }

    def gatherGoodsNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select sum(goods_num) as goodsNum from data_analysis " +
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
        def goodsNum = sql.firstRow(query).goodsNum
        goodsNum ?: 0
    }

    def gatherHoursMoneyNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select complete_hour as hour,round(sum(buy_price*goods_num),2) as money from data_analysis " +
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
        query += " group by complete_hour order by complete_hour asc"
        sql.rows(query)
    }

    def gatherHoursGoodsNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select complete_hour as hour,sum(goods_num) as num" +
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
        query += " group by complete_hour order by complete_hour asc"
        sql.rows(query)
    }

    def queryGoodsMoney(Long partnerId, String themeIds, String today, String yesterday, String orderBy, Integer pageNum, Integer pageSize) {
        def sql = new Sql(dataSource)
        def query = """
                    select distinct a.goods_name as goodsName,
                    ifnull(b.money, 0) as money1, ifnull(c.money, 0) as money2
                    from data_analysis a
                    left join
                    (select theme_store_goods_id as goodsId, round(sum(buy_price*goods_num),2) as money
                    from data_analysis where date(complete_time)='${today}' group by theme_store_goods_id) b
                    on a.theme_store_goods_id = b.goodsId
                    left join
                    (select theme_store_goods_id as goodsId, round(sum(buy_price*goods_num),2) as money
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
            query += " order by b.money desc"
        } else {
            query += " order by b.money asc"
        }
        sql.rows(query, (pageNum - 1) * pageSize, pageSize)
    }

    def queryGoodsCount(Long partnerId, String themeIds, String today, String yesterday, String orderBy, Integer pageNum, Integer pageSize) {
        def sql = new Sql(dataSource)
        def query = """
                    select distinct a.goods_name as goodsName,
                    ifnull(b.num, 0) as num1, ifnull(c.num, 0) as num2
                    from data_analysis a
                    left join
                    (select theme_store_goods_id as goodsId, sum(goods_num) as num
                    from data_analysis where date(complete_time)='${today}' group by theme_store_goods_id) b
                    on a.theme_store_goods_id = b.goodsId
                    left join
                    (select theme_store_goods_id as goodsId, sum(goods_num) as num
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
            query += " order by b.num desc"
        } else {
            query += " order by b.num asc"
        }
        sql.rows(query, (pageNum - 1) * pageSize, pageSize)
    }

    def queryGoods(Long partnerId, String themeIds, String fromDay, String toDay, String orderBy, Integer pageNum, Integer pageSize) {
        def sql = new Sql(dataSource)
        def query = "select goods_name,round(sum(buy_price*goods_num),2) as money, sum(goods_num) as num from data_analysis " +
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
        query += " group by goods_name"
        if (orderBy == "count") {
            query += " order by num desc"
        } else {
            query += " order by money desc"
        }
        sql.rows(query, (pageNum - 1) * pageSize, pageSize)
    }
}
