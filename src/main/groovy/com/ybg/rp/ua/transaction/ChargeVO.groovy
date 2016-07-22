package com.ybg.rp.ua.transaction

/**
 * Created by yangbagang on 16/7/22.
 * ping++付款参数VO
 */
class ChargeVO {

    /**
     * 订单号
     */
    String orderNo
    /**
     * 商户在ping++平台创建的appId
     */
    String appId
    /**
     * 第三方支付渠道
     */
    String channel
    /**
     * 订单总金额，单位为对应币种的最小货币单位
     */
    Integer amount
    /**
     * 发起支付请求终端的 IP 地址，格式为 IPV4，如: 127.0.0.1
     */
    String clientIp
    /**
     * 三位 ISO 货币代码，目前仅支持人民币 cny
     */
    String currency = "cny"
    /**
     * 商品的标题，该参数最长为 32 个 Unicode 字符
     */
    String subject
    /**
     * 商品的描述信息，该参数最长为 128 个 Unicode 字符
     */
    String body
    /**
     * 订单附加说明，最多 255 个 Unicode 字符
     */
    String description
    /**
     * 特定渠道发起交易时需要的额外参数以及部分渠道支付成功返回的额外参数
     */
    String extra
    /**
     * 订单失效时间，用 Unix 时间戳表示
     */
    Long timeExpire

}
