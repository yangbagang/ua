package com.ybg.rp.ua.utils

import com.pingplusplus.Pingpp
import com.pingplusplus.exception.PingppException
import com.pingplusplus.model.Charge
import com.ybg.rp.ua.transaction.ChargeVO
import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang.StringUtils

import javax.servlet.http.HttpServletRequest
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

/**
 * Created by yangbagang on 16/7/21.
 */
class PingPlusUtil {

    //交易失效间隔3分钟
    static ORDER_VALID_TIME = 180000L

    //交易的签名
    static apiKey = "sk_live_CaLmL0G8uvnHyLOij58aTaP4"

    //应用ID
    static appId = "app_ynjHOC8unXHGevD0"

    //回调的公钥PUB_KEY
    static publickKey = "" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv/r9xcQgdWizQzoZx0JP" +
            "C8Etnl82Qarm4ncb6kY1oYVk6VsdavH6kNZ8W3AV7/S8kRFhDQz8Foh+CkevARzi" +
            "KLLUOKi7dmfak6JXuc1NV/xtcWUZEWpgBlIdtSkP4llsGlRnhwGBOO5IplgTu/+S" +
            "DTs296juaHsNwUXDId/8+BMhf54KK113i5mBo8mLnCHd+miSDIxqP2+5wFcl3Vjm" +
            "H6nInjs7cfT5DAMP4qGz1KzvUvxnmlUv8yLUeJhsqvGgNcWRJSirKNG+pEVLSOKf" +
            "46aMrzoKbomkjaAM9gioIRpxsGgL4ghCGOP4MyiBeNv4exXKmCevenWNaLTg286P" +
            "LwIDAQAB"

    /**
     * 查询交易状态
     * @param chargeId
     * @param PING_API_KEY
     * @return
     * @throws Exception
     */
    static Charge retrieve(String chargeId) throws Exception {
        Pingpp.apiKey = apiKey
        Charge charge = Charge.retrieve(chargeId)
        return charge
    }

    /**
     * 支付
     *
     * @param request
     * @param price   金额
     * @param payType 支付类型：1、支付宝；2、微信
     * @param extra   特定渠道发起交易时需要的额外参数以及部分渠道支付成功返回的额外参数
     * @param subject 标题
     * @param body    内容
     * @return
     */
    static Map<String, Object> pay(String ip, Float price, String payType, String extra, String subject,
                                   String body, String orderNo) {
        def vo = new ChargeVO()
        vo.orderNo = orderNo
        Integer money = (price * 100).intValue()//将元转为分
        vo.amount = money
        vo.appId = appId
        if ("1".equals(payType)) {
            // 支付宝扫码
            vo.channel = ChannelCommon.ALIPAY_QR
            vo.setExtra(extra);// 特定渠道发起交易时需要的额外参数以及部分渠道支付成功返回的额外参数
        } else if ("2".equals(payType)) {
            // 微信扫码
            vo.channel = ChannelCommon.WX_PUB_QR
            vo.setExtra(extra);// 特定渠道发起交易时需要的额外参数以及部分渠道支付成功返回的额外参数
        }
        vo.clientIp = ip
        long timeExpire = (System.currentTimeMillis() + ORDER_VALID_TIME) / 1000
        vo.timeExpire = timeExpire
        vo.subject = subject
        vo.body = body// 内容
        vo.description = "Description"// 描述
        Map<String, Object> charge = charge(vo)
        return charge;
    }

    /**
     * ping++支付
     * @param chargeVO
     * @param PING_API_KEY
     * @return
     */
    public static Map<String, Object> charge(ChargeVO chargeVO) {
        def map = [:]
        map.success = false
        def chargeParams = [:]
        if (chargeVO != null) {
            chargeParams.order_no = chargeVO.orderNo
            def appMap = [:]
            appMap.id = chargeVO.appId
            chargeParams.app = appMap
            chargeParams.amount = chargeVO.amount
            chargeParams.channel = chargeVO.channel
            chargeParams.currency = chargeVO.currency
            chargeParams.client_ip = chargeVO.clientIp
            chargeParams.subject = chargeVO.subject
            chargeParams.body = chargeVO.body
            chargeParams.description = chargeVO.description
            Map<String, Object> extraMap = new HashMap<String, Object>();
            /**
             * 如果支付渠道为微信公众号支付，则该参数为用户在微信公众号的唯一标识
             * */
            if (StringUtils.equals(ChannelCommon.WX, chargeVO.getChannel())) {

            }else if (StringUtils.equals(ChannelCommon.ALIPAY, chargeVO.getChannel())) {
                //支付宝

            }else if (StringUtils.equals(ChannelCommon.WX_PUB, chargeVO.getChannel())) {
                //微信公众号支付

            }else if (StringUtils.equals(ChannelCommon.ALIPAY_WAP, chargeVO.getChannel())) {
                //支付宝手机网页支付
                chargeParams.put("extra", extraMap)
            }else if (StringUtils.equals(ChannelCommon.WX_PUB_QR, chargeVO.getChannel())) {
                //微信
                extraMap.put("product_id", chargeVO.getExtra())
                chargeParams.put("extra", extraMap)
            }
            if (chargeVO.getTimeExpire() != null) {
                chargeParams.put("time_expire", chargeVO.getTimeExpire())
            }
            try {
                // 发起交易请求
                Pingpp.apiKey = apiKey
                Charge charge = Charge.create(chargeParams)
                map.charge = charge
                map.success = true
            } catch (PingppException e) {
                e.printStackTrace()
                map.msg = e.getMessage()
                map.success = false
            }
        }
        return map
    }

    /**
     * 生成公钥类
     *
     * @return
     * @throws Exception
     */
    static PublicKey getPubKey() throws Exception {
        String pubKey = publickKey
        pubKey = pubKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "")
        byte[] keyBytes = Base64.decodeBase64(pubKey)
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes)
        KeyFactory keyFactory = KeyFactory.getInstance("RSA")
        PublicKey publicKey = keyFactory.generatePublic(spec)
        publicKey
    }

    static Map<String, byte[]> getInfo(HttpServletRequest request) throws Exception {
        Map<String, byte[]> map = new HashMap<String, byte[]>()
        request.setCharacterEncoding("UTF-8")
        byte[] fileBytes = request.getHeader("x-pingplusplus-signature")?.getBytes()
        String header = new String(fileBytes, "UTF-8")
        fileBytes = Base64.decodeBase64(header.getBytes())
        map.put("sign", fileBytes)
        BufferedReader reader = request.getReader()
        StringBuffer buffer = new StringBuffer()
        String string = ""
        while ((string = reader.readLine()) != null) {
            buffer.append(string)
        }
        reader.close()
        println "buffer=$buffer"
        fileBytes = buffer.toString().getBytes()
        map.put("data", fileBytes)
        return map
    }

    /**
     * 验证签名
     *
     * @param data
     * @param sigBytes
     * @param publicKey
     * @return
     * @throws Exception
     */
    static boolean verifyData(byte[] data, byte[] sigBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(sigBytes)
    }

}
