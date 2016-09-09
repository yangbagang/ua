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

    def gatherUserNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select count(distinct pay_account) as userNum from data_analysis " +
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
        def userNum = sql.firstRow(query).userNum
        userNum ?: 0
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

    def queryNewUserNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select count(distinct a.pay_account) as userNum from data_analysis a " +
                "where a.partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and a.theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and a.complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and a.complete_time <= '${toDay} 23:59:59'"
        }
        def sub = " and not exists (select * from data_analysis b where (b.complete_time <= '${fromDay} 00:00:00' " +
                " or b.complete_time >= '${toDay} 23:59:59') and a.pay_account=b.pay_account)"
        query += sub
        def userNum = sql.firstRow(query).userNum
        userNum ?: 0
    }

    def queryNewUserMoney(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select sum(goods_num*buy_price) as money from data_analysis a " +
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
        def sub = " and not exists (select * from data_analysis b where (b.complete_time <= '${fromDay} 00:00:00' " +
                " or b.complete_time >= '${toDay} 23:59:59') and a.pay_account=b.pay_account)"
        query += sub
        def money = sql.firstRow(query).money
        money ? money.round(2) : 0
    }

    def queryNewUserOrder(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select count(distinct order_no) as orderNum from data_analysis a " +
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
        def sub = " and not exists (select * from data_analysis b where (b.complete_time <= '${fromDay} 00:00:00' " +
                " or b.complete_time >= '${toDay} 23:59:59') and a.pay_account=b.pay_account)"
        query += sub
        def orderNum = sql.firstRow(query).orderNum
        orderNum ?: 0
    }

    def queryPayWayNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select pay_way,count(distinct order_no) as num from data_analysis a " +
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
        def sub = " group by pay_way order by pay_way"
        query += sub
        def num1 = 0
        def num2 = 0
        sql.eachRow(query) {row ->
            def pay_way = row.pay_way
            if (pay_way == 1) {
                num1 += row.num
            } else if (pay_way == 2) {
                num2 += row.num
            }
        }
        def dataList = [["num":num1], ["num":num2]]
        dataList
    }

    def queryMoneyNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select pay_account, sum(goods_num*buy_price) as num from data_analysis a " +
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
        def sub = " group by pay_account order by num"
        query += sub
        def num1 = 0
        def num2 = 0
        def num3 = 0
        def num4 = 0
        def num5 = 0
        sql.eachRow(query) {row ->
            def num = row.num
            if (num <= 10) {
                num1 += 1
            } else if (num <= 50) {
                num2 += 1
            } else if (num <= 100) {
                num3 += 1
            } else if (num <= 200) {
                num4 += 1
            } else {
                num5 += 1
            }
        }
        def dataList = [["num":num1], ["num":num2], ["num":num3], ["num":num4], ["num":num5]]
        dataList
    }

    def queryNumberNum(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select pay_account,count(distinct order_no) as num from data_analysis a " +
                " where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        def sub = " group by pay_account order by num"
        query += sub
        def num1 = 0
        def num2 = 0
        def num3 = 0
        def num4 = 0
        def num5 = 0
        sql.eachRow(query) {row ->
            def num = row.num
            if (num <= 10) {
                num1 += 1
            } else if (num <= 50) {
                num2 += 1
            } else if (num <= 100) {
                num3 += 1
            } else if (num <= 200) {
                num4 += 1
            } else {
                num5 += 1
            }
        }
        def dataList = [["num":num1], ["num":num2], ["num":num3], ["num":num4], ["num":num5]]
        dataList
    }

    def queryStoreData(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select theme_store_name as name, round(sum(buy_price*goods_num),2) as money, count(distinct order_no) as num" +
                " from data_analysis a where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        def sub = " group by theme_store_name order by money desc"
        query += sub
        sql.rows(query)
    }

    def queryStoreName(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select distinct theme_store_name as name " +
                " from data_analysis a where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        def nameList = []
        sql.eachRow(query) { row ->
            nameList.add(row.name)
        }
        nameList
    }

    def queryCompleteDay(Long partnerId, String themeIds, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select distinct date(complete_time) as day " +
                " from data_analysis a where partner_id=" + partnerId
        if (themeIds != null && themeIds != "") {
            query += " and theme_store_id in (${themeIds})"
        }
        if (fromDay != null && fromDay != "") {
            query += " and complete_time >= '${fromDay} 00:00:00'"
        }
        if (toDay != null && toDay != "") {
            query += " and complete_time <= '${toDay} 23:59:59'"
        }
        query += " order by day asc"
        def dayList = []
        sql.eachRow(query) { row ->
            dayList.add(row.day as String)
        }
        dayList
    }

    def queryStoreCount(String name, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select b.storeName, ifnull(b.num,0) as num,a.completeDate from " +
                    " (select distinct date(complete_time) as completeDate from data_analysis " +
                    " where complete_time >= '${fromDay} 00:00:00' and complete_time <= '${toDay} 23:59:59' " +
                    " order by completeDate) a left join " +
                    " (select theme_store_name as storeName, count(distinct order_no) as num, date(complete_time) " +
                    " as completeDate from data_analysis where theme_store_name='${name}' group by completeDate) b " +
                    " on a.completeDate=b.completeDate"
        def numList = []
        sql.eachRow(query) { row ->
            numList.add(row.num)
        }
        numList
    }

    def queryStoreMoney(String name, String fromDay, String toDay) {
        def sql = new Sql(dataSource)
        def query = "select b.storeName, ifnull(b.num,0) as money,a.completeDate from " +
                    " (select distinct date(complete_time) as completeDate from data_analysis " +
                    " where complete_time >= '${fromDay} 00:00:00' and complete_time <= '${toDay} 23:59:59' " +
                    " order by completeDate) a left join " +
                    " (select theme_store_name as storeName, round(sum(buy_price*goods_num),2) as num, date(complete_time) " +
                    " as completeDate from data_analysis where theme_store_name='${name}' group by completeDate) b " +
                    " on a.completeDate=b.completeDate"
        def numList = []
        sql.eachRow(query) { row ->
            numList.add(row.money)
        }
        numList
    }

}
