package com.ybg.rp.ua.themeStore

import com.ybg.rp.ua.base.GoodsTypeOne
import com.ybg.rp.ua.device.VendMachineInfo
import grails.transaction.Transactional
import org.hibernate.Query

@Transactional
class ThemeStoreGoodsInfoService {

    def listGoodsByName(ThemeStoreBaseInfo themeStoreBaseInfo, String name) {
        def hql = "select new map(t.name as goodsName, t.realPrice as price, t.id as id, t.letter as goodsInitials)" +
                " from ThemeStoreGoodsInfo t where t.themeStore=? and t.status=1 and t.name like ? order by t.letter asc"
        ThemeStoreGoodsInfo.executeQuery(hql, [themeStoreBaseInfo, "%${name}%"])
    }

    def listGoods(ThemeStoreBaseInfo themeStoreBaseInfo) {
        def hql = "select new map(t.name as goodsName, t.realPrice as price, t.id as id, t.letter as goodsInitials)" +
                " from ThemeStoreGoodsInfo t where t.themeStore=? and t.status=1 order by t.letter asc"
        ThemeStoreGoodsInfo.executeQuery(hql, [themeStoreBaseInfo])
    }

}
