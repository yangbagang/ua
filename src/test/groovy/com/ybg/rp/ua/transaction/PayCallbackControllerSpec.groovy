package com.ybg.rp.ua.transaction

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PayCallbackController)
class PayCallbackControllerSpec extends Specification {

    def json = """
                {"id":"evt_NGHrC1i4whIozt6uR4QIauf6","created":1473209951,"livemode":true,"type":"charge.succeeded","data":{"object":{"id":"ch_nv9SiDDy5qHK0SSK44rbr50C","object":"charge","created":1473209943,"livemode":true,"paid":true,"refunded":false,"app":"app_ynjHOC8unXHGevD0","channel":"alipay_qr","order_no":"201609070857200830","client_ip":"112.97.61.167","amount":2239,"amount_settle":2226,"currency":"cny","subject":"购买商品-201609070857200830","body":"售卖机线下购买商品","extra":{"buyer_account":"497***@qq.com"},"time_paid":1473209950,"time_expire":1473210021,"time_settle":null,"transaction_no":"2016090721001004880234339738","refunds":{"object":"list","url":"/v1/charges/ch_nv9SiDDy5qHK0SSK44rbr50C/refunds","has_more":false,"data":[]},"amount_refunded":0,"failure_code":null,"failure_msg":null,"metadata":{},"credential":{},"description":"Description"}},"object":"event","pending_webhooks":1,"request":"iar_ajDyv1WL0iXDbnfXf1ev5Ca1"}
               """

    def setup() {
    }

    def cleanup() {
    }

    void "test callback"() {
        when:
        request.method = 'POST'
        request.json = json
        controller.callback()

        then:
        response.status == SC_OK
    }
}
