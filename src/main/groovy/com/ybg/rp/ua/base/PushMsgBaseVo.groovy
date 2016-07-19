package com.ybg.rp.ua.base

/**
 * Created by yangbagang on 16/7/19.
 */
class PushMsgBaseVo {

    /** 打开柜门*/
    static TYPE_OPEN = "1"
    /** 出货*/
    static TYPE_OPEN_ONLINE = "2"
    /** 线下交易打开柜门*/
    static TYPE_OPEN_OFFLINE = "10002"
    /** 登录*/
    static TYPE_LOGIN = "10003"
    /**扫码后推送*/
    static TYPE_LOGIN_TS = "10004"
    //类型
    String type

}
