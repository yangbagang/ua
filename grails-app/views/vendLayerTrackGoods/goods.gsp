<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${themeStore?.name}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <asset:stylesheet src="framework7.material.min.css"/>
    <asset:stylesheet src="framework7.material.colors.min.css"/>
    <asset:stylesheet src="wx.css"/>
</head>

<body onload="getGoodsNumAndMoney('${params.openid}');">
<div class="page-content goods_list_bg">
    <div class="content-block">
        <div class="content-block-title">所有商品</div>
        <g:each status="i" var="g" in="${goods}">
            <g:if test="${i % 2 == 0}">
                <div class="row goods_list">
            </g:if>
            <div class="col-50 goods_card">
                <span class="goods_img">
                    <g:img dir="images" file="default.png"/>
                </span>
                <h4 class="goods_title">${g[2].name}</h4>

                <div class="add_wrap">
                    <p class="goods_price">¥<span>${g[2].realPrice}</span></p>
                    <span class="add-icon icon-add" data="${g[0]}"></span>
                </div>

                <p class="buttons-row">
                    <input type="button" class="button button-raised button-fill color-cyan ripple-pink" value="添加"
                           onclick="addToCart('${params.openid}', ${g[2].id});">
                    <input type="button" class="button button-raised button-fill color-teal ripple-yellow" value="详情"
                           onclick="buySingleGoods('${params.openid}', ${g[2].id});">
                </p>
            </div>
            <g:if test="${i % 2 != 0}">
                </div>
            </g:if>
        </g:each>
    </div>
    <br /><br />
    <!-- 底部选项卡 -->
    <div class="toolbar cart-bar">
        <g:img dir="images" file="shoppingCart.png" width="44" height="44" onclick="showCart(${themeStore?.id}, '${params.openid}');"/>
        <span class="cart-label" onclick="showCart(${themeStore?.id}, '${params.openid}');">去结算</span>
        <span class="badge" id="cartNum">0</span>
    </div>
</div>
</body>
<asset:javascript src="framework7.min.js"/>
<asset:javascript src="my-app.js"/>
</html>