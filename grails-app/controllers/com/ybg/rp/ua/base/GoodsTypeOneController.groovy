package com.ybg.rp.ua.base

import grails.converters.JSON

class GoodsTypeOneController {

    def listAll() {
        def map = [:]
        map.success = true
        map.list = GoodsTypeOne.findAllByStatus(1 as Short)
        render map as JSON
    }
}
