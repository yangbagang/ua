package com.ybg.rp.ua.transaction

import com.pingplusplus.model.Charge
import com.ybg.rp.ua.utils.PingPlusUtil
import grails.converters.JSON
import grails.transaction.Transactional

import java.text.SimpleDateFormat

class OrderInfoController {

    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    def orderInfoService

    /**
     * 更新订单状况
     * @param orderNo
     * @param result
     * @param transDate
     * @param isDataUp
     * @param cancelType
     * @return
     */
    @Transactional
    def updateOrderStatus(String orderNo, String result, String transDate, boolean isDataUp, Short cancelType) {
        def map = [:]
        def orderInfo = OrderInfo.findByOrderNo(orderNo)
        if (!orderInfo) {
            map.success = false
            map.msg = "没有该订单数据-${orderNo}"
            render map as JSON
            return
        }
        if ("0".equals(result)) {//取消订单
            if (orderInfo.transNo) {
                orderInfo.deliveryStatus = 1 as Short
            } else {
                if (orderInfo.payStatus == 1) {
                    map.success = true
                    map.msg = "接收成功"
                    render map as JSON
                    return
                }
                if (orderInfo.isCancel == 0) {
                    if (cancelType != 0) {
                        orderInfo.isCancel = cancelType
                    } else {
                        orderInfo.isCancel = 1 as Short
                    }
                }
            }
            orderInfo.completeTime = new Date()
        } else if ("1".equals(result)) {//出货成功
            orderInfo.deliveryStatus = 1 as Short
            orderInfo.completeTime = sdf.parse(transDate)
            if (isDataUp) {
                orderInfoService.updateCurrentInventory(orderInfo)
            }
        } else if ("2".equals(result)) {//出货失败
            orderInfo.deliveryStatus = 2 as Short
        }
        orderInfo.save flush: true
        map.success = true
        map.msg = "接收成功"
        render map as JSON
    }

    /**
     * 标记出货失败
     * @param orderSn
     * @param trackNo
     * @param gid
     * @return
     */
    @Transactional
    def deliveryGoodsFail(String orderSn, String trackNo, Long gid) {
        orderInfoService.deliveryGoodsFail(orderSn, trackNo, gid)

        def map = [:]
        map.success = true
        map.msg = "上传成功"
        render map as JSON
    }

    /**
     * 售卖机上单选某个商品生成订单。
     * @param machineId
     * @param trackNo
     * @return
     */
    @Transactional
    def createOrderWithMachineIdAndTrackNo(Long machineId, String trackNo) {
        def map = orderInfoService.createOrderWithSingleGoods(machineId, trackNo)
        render map as JSON
    }

    /**
     * 购物车购买
     * @param machine
     * @param goodsJson
     * @return
     */
    @Transactional
    def createOrderWithMchineIdAndGoodsJson(Long machine, String goodsJson) {
        def map = orderInfoService.createOrder(machine, goodsJson)
        render map as JSON
    }

    /**
     * 查询某个订单是否己经成功支付
     * @param orderSn
     */
    def queryOrderIsPay(String orderSn) {
        def map = [:]
        def orderInfo = OrderInfo.findByOrderNo(orderSn)
        if (orderInfo) {
            def transNo = orderInfo.transNo
            if (transNo != null && !"".equals(transNo)) {//有交易号代表己经支付
                map.success = true
                map.isPay = true
                map.deliveryStatus = orderInfo.deliveryStatus
                map.payWay = orderInfo.payWay
            } else {
                //未查到交易号,有可能是ping++没有回调webhook
                //此处进行主动查询,如果己经支付则更新状态并返回
                def transaction = TransactionInfo.findByOrderNo(orderSn)
                if (transaction) {
                    try {
                        //查询交易数据
                        Charge charge = PingPlusUtil.retrieve(transaction.chargeId)
                        if (charge.getPaid()) {
                            Map<String, String> extraMap = charge.getExtra()
                            if ("1".equals(transaction.payType)) {
                                transaction.payAccount = extraMap.get("buyer_account")// 支付宝支付账号
                            } else if ("2".equals(transaction.payType)) {
                                transaction.payAccount = extraMap.get("open_id")// 微信openid
                            }
                            //更新订单状态
                            orderInfoService.updateOrderStatus(orderInfo, transaction, charge.getTransactionNo())
                            //扣库存
                            orderInfoService.updateCurrentInventory(orderInfo)

                            map.put("isPay", true);
                        } else {
                            map.put("isPay", false);
                        }
                    } catch (Exception e) {
                        map.put("isPay", false);
                        println "********queryOrderInfo********查询charge报错:" + e.getMessage()
                    }
                } else {
                    map.isPay = false
                }
            }
        } else {
            map.success = false
            map.msg = "订单号有误"
        }
        render map as JSON
    }

    /**
     * 创建ping++订单,返回信息将用于生成二维码。
     * @param machineId
     * @param orderNo
     * @param payType
     */
    @Transactional
    def createPingPlusCharge(Long machineId, String orderNo, String payType) {
        def map = [:]
        if (machineId && orderNo && payType) {
            try {
                def orderInfo = OrderInfo.findByOrderNo(orderNo)
                orderInfo.payWay = Short.valueOf(payType)
                map = PingPlusUtil.pay(request.getRemoteAddr(), orderInfo.realMoney, payType, orderNo, "购买商品-${orderNo}", "售卖机线下购买商品", orderNo)
                Charge charge = (Charge) map.get("charge")
                //记录chargeId,以及订单号,金额等信息-到支付记录表
                if (charge) {
                    def transactionInfo = TransactionInfo.findByOrderNo(orderNo)
                    if (!transactionInfo) {
                        transactionInfo = new TransactionInfo()
                        transactionInfo.orderNo = orderNo
                    }
                    transactionInfo.chargeId = charge.getId()
                    transactionInfo.orderMoney = orderInfo.realMoney
                    transactionInfo.payType = payType
                    transactionInfo.isSuccess = 0 as Short//是否支付成功 0：未支付；1：支付成功
                    transactionInfo.createTime = new Date()
                    transactionInfo.save flush: true
                    map.put("Charge", charge)
                    map.put("success", true)
                }
            } catch (Exception e) {
                e.printStackTrace()
                map.put("success", false)
                map.put("msg", e.getMessage())
            }
        } else {
            map.put("success", false)
            map.put("msg", "参数不能为空")
        }
        render map as JSON
    }

}
