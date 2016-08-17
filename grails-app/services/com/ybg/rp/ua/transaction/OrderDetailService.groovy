package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.partner.PartnerBaseInfo
import com.ybg.rp.ua.themeStore.ThemeStoreBaseInfo
import com.ybg.rp.ua.themeStore.ThemeStoreOfPartner
import com.ybg.rp.ua.utils.DateUtil
import grails.transaction.Transactional

@Transactional
class OrderDetailService {

    def updateStatus(OrderInfo orderInfo, Short status) {
        if (orderInfo) {
            def details = OrderDetail.findAllByOrder(orderInfo)
            for (OrderDetail detail: details) {
                detail.status = status
                detail.save flush: true
            }
        }
    }

    def listOrderDetail(PartnerBaseInfo partner, Map map, String themeIds, String startDate, String endDate, Integer pageSize, Integer pageNum) {
        //查询范围
        def themeStores
        if (themeIds == null || "" == themeIds) {
            themeStores = ThemeStoreOfPartner.findAllByPartner(partner)
        } else {
            themeStores = getThemeStores(themeIds)
        }

        //查询
        def c = OrderDetail.createCriteria()
        def result = c.list(max: pageSize, offset: (pageNum - 1) * pageSize) {
            order {
                and {
                    vendMachine{
                        'in'("themeStore", themeStores)
                    }
                    gt("completeTime", DateUtil.getFromTime(startDate))
                    lt("completeTime", DateUtil.getToTime(endDate))
                    eq("payStatus", 1 as Short)
                }
            }
        }

        //构造返回数据集
        def list = []
        for (OrderDetail detail : result.resultList) {
            def vo = [:]
            vo.detailId = detail.id
            vo.orderTime = detail.order.completeTime
            vo.goodName = detail.goodsName
            vo.goodNum = detail.goodsNum
            vo.storeName = detail.goods?.vendMachine?.themeStore?.name
            vo.orbitalNo = detail.orbitalNo
            vo.price = detail.buyPrice
            vo.transWay = detail.order.payWay
            list.add(vo)
        }

        //构造分页结果
        def totalCount = result.size()
        def pageCount = 0
        if (totalCount % pageSize == 0) {
            pageCount = (int) (totalCount / pageSize)
        } else {
            pageCount = (int) (totalCount / pageSize) + 1
        }

        //构造数据
        map.currPage = pageNum
        map.pageSize = pageSize
        map.totalPageCount = totalCount
        map.dataList = list
    }

    private getThemeStores(String themeIds) {
        def ids = []
        def tIds = themeIds.split(",")
        for (String id: tIds) {
            if ("" != id) {
                try {
                    ids.add(Long.valueOf(id))
                } catch (Exception e) {
                    //nothing
                }
            }
        }
        ThemeStoreBaseInfo.findAllByIdInList(ids)
    }
}
