package com.ybg.rp.ua.utils

import com.gexin.rp.sdk.base.IPushResult
import com.gexin.rp.sdk.base.impl.SingleMessage
import com.gexin.rp.sdk.base.impl.Target
import com.gexin.rp.sdk.base.payload.APNPayload
import com.gexin.rp.sdk.exceptions.RequestException
import com.gexin.rp.sdk.http.IGtPush
import com.gexin.rp.sdk.template.TransmissionTemplate

/**
 * Created by yangbagang on 16/7/19.
 */
class MsgPushHelper {

    static appId = "4JCYuVTzz37viY08wQyu81"
    static appkey = "dYYkFFqDmc9cAStDjaopc"
    static master = "VSu5GEYc2P7GnVfaWhhAq1"
    static secret = "FdN8HjlrvH8oyCfIfODfp8"
    static host = "http://sdk.open.api.igexin.com/apiex.htm"

    /** 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动*/
    private static transmissionType = 2
    /**可选。判断是否客户端是否wifi环境下推送，1为在WIFI环境下，默认：0为不限制网络环境。*/
    private static pushNetWorkType = 0

    /**
     * 个推消息发送
     *
     * @param clientId
     * @param content
     * @return
     */
    public boolean sendMsg(String clientId, String content) {
        IGtPush push = new IGtPush(host, appkey, master);
        SingleMessage message = new SingleMessage();
        message.setData(transmissionTemplateFromContent(content));
        message.setOfflineExpireTime(1000 * 20);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            println("调用个推服务异常，clientId: ${clientId}, content: ${content}");
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            println("ret  个推返回信息：" + ret.getResponse().toString());
            if (ret.getResponse().toString().contains("successed_online")
                    || ret.getResponse().toString().contains("successed_offline")) {
                return true;
            } else {
                //logger.info("个推错误原因：" + ret.getResponse().toString() + ",MsgPushParams  " + params);
            }
        } else {
            //logger.info("服务器响应异常 ,MsgPushParams  " + params);
        }
        return false;
    }

    public static TransmissionTemplate transmissionTemplateFromContent(String content) {
        TransmissionTemplate template = new TransmissionTemplate()
        template.setAppId(appId)
        template.setAppkey(appkey)
        //logger("content" + content, INFO)
        template.setTransmissionType(transmissionType)
        template.setTransmissionContent(content);
        APNPayload payload = new APNPayload();
        payload.setBadge(1);
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("由客户端定义");
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));
        // 字典模式使用下者
        // payload.setAlertMsg(getDictionaryAlertMsg());
        template.setAPNInfo(payload);
        return template;
    }

}
