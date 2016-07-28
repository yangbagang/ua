package com.ybg.rp.ua.analysis

class DataAnalysis {

    static constraints = {
        themeStoreId nullable: true
        themeStoreName nullable: true
        partnerId nullable: true
        partnerName nullable: true
        machineId nullable: true
        machineCode nullable: true
        orderId nullable: true
        orderNo nullable: true
        goodsName nullable: true
        baseGoodsId nullable: true
        themeStoreGoodsId nullable: true
        goodsPic nullable: true
        goodsSpec nullable: true
        typeOneId nullable: true
        typeTwoId nullable: true
        typeOneName nullable: true
        typeTwoName nullable: true
        orbitalNo nullable: true
        goodsNum nullable: true
        buyPrice nullable: true
        payWay nullable: true
        orderWay nullable: true
        completeTime nullable: true
        completeYear nullable: true
        completeMonth nullable: true
        completeDay nullable: true
        completeHour nullable: true
        dayInWeek nullable: true
        weekInYear nullable: true
        payAccount nullable: true
    }

    //主题店
    Long themeStoreId
    String themeStoreName
    //合作伙伴
    Long partnerId
    String partnerName
    //机器
    Long machineId
    String machineCode
    //订单
    Long orderId
    String orderNo
    //商品
    String goodsName
    Long baseGoodsId
    Long themeStoreGoodsId
    String goodsSpec
    String goodsPic
    //商品分类
    Long typeOneId
    String typeOneName
    Long typeTwoId
    String typeTwoName
    //轨道
    String orbitalNo
    //数量
    Integer goodsNum
    //单价
    Float buyPrice
    //支付方式
    Short payWay//0:银联 1:支付宝 2:微信支付 3:在线账户
    //下单方式
    Short orderWay//0:线下 1:微信 2:WEB 3:APP
    //完成时间
    Date completeTime
    Integer completeYear//年
    Integer completeMonth//月
    Integer completeDay//日
    Integer completeHour//时
    Integer dayInWeek//星期几
    Integer weekInYear//第几周
    /**支付宝支付：支付宝账号  微信支付：微信openId  **/
    String payAccount
}
