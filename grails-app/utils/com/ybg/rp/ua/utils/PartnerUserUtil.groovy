package com.ybg.rp.ua.utils

import org.apache.commons.codec.digest.DigestUtils

/**
 * Created by yangbagang on 16/7/16.
 */
class PartnerUserUtil {

    private static Map container = [:]

    public static String createUserTokenFromId(Long partnerUserId) {
        def token = DigestUtils.sha256Hex("${System.currentTimeMillis()}-partner-user")
        container.put(partnerUserId, token)
        token
    }

    public static boolean checkTokenValid(String token) {
        return container.containsValue(token)
    }

    public static Long getPartnerUserIdFromToken(String token) {
        if (!checkTokenValid(token)) {
            return 0
        }
        def key = container.find{it.value == token}?.key
        return key
    }
}
