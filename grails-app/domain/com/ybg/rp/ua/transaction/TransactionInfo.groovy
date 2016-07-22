package com.ybg.rp.ua.transaction

class TransactionInfo {

    static constraints = {
        updateTime nullable: true
    }

    /**Charge ID*/
    String chargeId
    /**订单金额(元) */
    Float orderMoney
    /**订单号*/
    String orderNo
    /**支付类型  0 卡1、支付宝；2、微信*/
    String payType
    /**是否支付成功 0：未支付；1：支付成功*/
    Short isSuccess = 0 as Short
    /**支付宝支付：支付宝账号  微信支付：微信openId  **/
    String payAccount = ""
    /**创建时间*/
    Date createTime = new Date()
    /**更新时间*/
    Date updateTime
}
