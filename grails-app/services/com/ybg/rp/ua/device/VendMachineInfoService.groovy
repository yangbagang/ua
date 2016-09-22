package com.ybg.rp.ua.device

import com.ybg.rp.ua.partner.PartnerUserInfo
import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional(readOnly = true)
class VendMachineInfoService {

    def dataSource

    def list(PartnerUserInfo userInfo) {
        String query = "select a.id,a.machine_code,b.name,a.online_status,a.report_time from vend_machine_info a " +
                "left join theme_store_base_info b on a.theme_store_id=b.id " +
                "left join partner_user_store c on b.id=c.store_id where c.user_id=${userInfo.id} and a.is_real=1"
        def sql = new Sql(dataSource)
        sql.rows(query)
    }
}
