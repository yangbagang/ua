package com.ybg.rp.ua.base

import grails.converters.JSON

class GoodsTypeTwoController {

    /**
     * 列出指定大类下的小类
     * @param typeOneId
     * @return
     */
    def listAll(Long typeOneId) {
        def map = [:]
        def typeOne = GoodsTypeOne.get(typeOneId)
        map.success = true
        map.list = GoodsTypeTwo.findAllByTypeOneAndStatus(typeOne, 1 as Short)
        render map as JSON
    }

}
