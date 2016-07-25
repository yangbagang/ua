package com.ybg.rp.ua.base

import grails.converters.JSON

class GoodsTypeOneController {

    /**
     * 列出己启用的大类
     * @return
     */
    def listAll() {
        def map = [:]
        map.success = true
        map.list = GoodsTypeOne.findAllByStatus(1 as Short)
        render map as JSON
    }

}
