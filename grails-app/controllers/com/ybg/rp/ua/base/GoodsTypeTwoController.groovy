package com.ybg.rp.ua.base

import grails.converters.JSON

class GoodsTypeTwoController {

    def listAll(Long typeOneId) {
        def map = [:]
        def typeOne = GoodsTypeOne.get(typeOneId)
        map.success = true
        map.list = GoodsTypeTwo.findAllByTypeOneAndStatus(typeOne, 1 as Short)
        render map as JSON
    }
}
