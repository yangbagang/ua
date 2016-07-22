package com.ybg.rp.ua.transaction

import com.ybg.rp.ua.device.VendMachineInfo

class OrderInfo {

    static constraints = {
        isCancel nullable: true
        payStatus nullable: true
        orderWay nullable: true
        deliveryStatus nullable: true
        payWay nullable: true
        getWay nullable: true
        transNo nullable: true
        peiSongMoney nullable: true
        confirmTime nullable: true
        completeTime nullable: true
        quHuoCode nullable: true
        peiSongTime nullable: true
        quHuoTime nullable: true
    }

    VendMachineInfo vendMachine
    String orderNo
    /** 作废标识 */
    Short isCancel = 0 as Short//0:未取消 1:手动取消 2:超时取消
    /** 付款状态  */
    Short payStatus = 0 as Short //0:未付款 1:已付款
    /** 订单渠道 */
    Short orderWay = 0 as Short  //0:线下 1:微信 2:WEB 3:APP
    /** 发货状态 */
    Short deliveryStatus = 0 as Short   //0:未发货 1:出货成功 2:出货失败
    /** 支付方式 */
    Short payWay   //0:银联 1:支付宝 2:微信支付 3:在线账户
    /** 取货方式 */
    Short getWay = 1 as Short //0:默认 未选 1:自己取 2:别人配送

    String transNo
    Float orderMoney = 0
    Float peiSongMoney = 0
    Float totalMoney = 0
    Float youHuiJuan = 0
    Float userScore = 0
    Float realMoney = 0
    Date createTime
    Date confirmTime//支付时间
    Date completeTime//出货时间
    Date quHuoTime
    Date peiSongTime
    String quHuoCode
}
