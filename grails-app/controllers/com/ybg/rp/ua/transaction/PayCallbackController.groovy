package com.ybg.rp.ua.transaction

import com.pingplusplus.model.Charge
import com.pingplusplus.model.Webhooks
import com.ybg.rp.ua.base.PushMsgBaseVo
import com.ybg.rp.ua.device.VendMachineInfo
import com.ybg.rp.ua.utils.MsgPushHelper
import com.ybg.rp.ua.utils.PingPlusUtil
import grails.converters.JSON

import java.security.PublicKey

class PayCallbackController {

    def orderInfoService

    /**
     * PING++支付回调 - Webhooks payCallback/callback
     */
    def callback() {
        Map<String, byte[]> map = PingPlusUtil.getInfo(request);
        byte[] data = map.get("data");
        byte[] sign = map.get("sign");
        PublicKey publicKey = PingPlusUtil.getPubKey();
        boolean flag = PingPlusUtil.verifyData(data, sign, publicKey);
        // 不进行 签名校验
        flag = true;
        if (flag) {
            Object obj = Webhooks.getObject(new String(data, "UTF-8"));
            if (obj instanceof Charge) {
                Charge charge = (Charge) obj;
                /**
                 * 根据ping++异步通知，如果支付成功，修改订单状态
                 * */
                if (charge.paid) {//己经支付
                    String transaction_no = charge.transactionNo
                    // 支付成功-需要操作的步骤
                    TransactionInfo transactionInfo = TransactionInfo.findByChargeId(charge.id);
                    if(transactionInfo?.isSuccess == (1 as Short)){//如果已支付就不执行下面的操作
                        response.setStatus(200);
                        return;
                    }
                    if (null != transactionInfo) {
                        /** 新增设置-存储支付账号 */
                        Map<String, Object> extraMap = charge.extra
                        if ("1" == transactionInfo.payType) {
                            transactionInfo.payAccount = extraMap.get("buyer_account").toString();// 支付宝支付账号
                        } else if ("2" == transactionInfo.payType) {
                            transactionInfo.payAccount = extraMap.get("open_id").toString();// 微信openid
                        }
                        //更新订单状态
                        OrderInfo orderInfo = OrderInfo.findByOrderNo(transactionInfo.orderNo)
                        orderInfoService.updateOrderStatus(orderInfo, transactionInfo, transaction_no)
                        //扣库存
                        orderInfoService.updateCurrentInventory(orderInfo)
                        //推送消息
                        def details = OrderDetail.findAllByOrder(orderInfo)
                        def pushOrderVo = [:]
                        pushOrderVo.orderNo = orderInfo.orderNo
                        pushOrderVo.orderMoney = orderInfo.realMoney
                        def orderInfos = []
                        for (OrderDetail orderDetail : details) {//获取订单的相关数据
                            def order = [:]
                            /**设置属性 start***/
                            order.gid = orderDetail.goods.goods.id
                            order.goodsName = orderDetail.goodsName
                            order.goodsPic = orderDetail.goodsPic
                            order.num = orderDetail.goodsNum
                            order.trackNo = orderDetail.orbitalNo
                            order.price = orderDetail.buyPrice
                            order.goodsDesc = orderDetail.goodsSpec
                            /**设置属性 end***/
                            orderInfos.add(order);
                        }
                        pushOrderVo.orderInfos = orderInfos//二选一,新版上线后删除此行。//TODO
                        pushOrderVo.goodsInfo = orderInfos
                        pushOrderVo.type = PushMsgBaseVo.TYPE_OPEN_OFFLINE
                        VendMachineInfo vendMachineInfo = orderInfo.vendMachine
                        //个推
                        def clientId = vendMachineInfo.clientId
                        def content = pushOrderVo as JSON
                        MsgPushHelper msgPushHelper = new MsgPushHelper();
                        if(msgPushHelper.sendMsg(clientId, content as String)){
                            println "回调推送开门 ----->操作成功"
                        }else {
                            println "回调推送开门 ----->开门失败"
                        }
                    } else {
                        println "----没有找到改订单数据----" + charge.id
                    }
                } else {
                    println "----PING++ 交易失败----" + charge.id
                }
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(500);
        }
        render ""
    }
}
