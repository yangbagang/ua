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

<body onload="sumCart();">
<div class="page-content goods_list_bg">
    <div>
        <ul class="shop-cart">
            <g:each var="cart" in="${cartList}">
            <li class="cart-item" id="item-${cart.goodsInfo?.id}" data-price="${cart.goodsInfo?.realPrice}" data-num="${cart.num}">
                <div class="pull-left checkboxButtom cart-checkbox" onclick="checkItem(${cart.goodsInfo?.id});">
                    <input type="checkbox" checked="checked" value="${cart.id}" style="display: none;">
                    <label></label>
                </div>
                <g:img dir="images" file="default.png" width="64" height="64" class="pull-left cart-img" />
                <div style="margin: 0 0.5rem;">
                    <div>
                        <h3 style="display: inline-block;">${cart.goodsInfo?.name}</h3>
                        <span class="icon-remove" style="width: 1.5rem; height: 1.5rem; margin-top: 1rem; float: right;"
                              onclick="removeGoods('${params.openid}', ${cart.goodsInfo?.id});"></span>
                    </div>
                    <div>
                        <span class="gray-font">${cart.goodsInfo?.specifications}</span>
                    </div>
                    <div>
                        <span class="price pull-left">￥${cart.goodsInfo?.realPrice * cart.num}</span>
                        <span class="gray-font pull-left money" style="margin-top: 0.1rem;">${cart.goodsInfo?.realPrice}*${cart.num}</span>
                        <div class="operator">
                            <span class="icon icon-minus pull-left" style="width: 1.5rem; height: 1.5rem;"
                                  onclick="goodsMinus('${params.openid}', ${cart.goodsInfo?.id});"></span>
                            <span class="pull-left goods-num">${cart.num}</span>
                            <span class="icon icon-plux pull-left" style="width: 1.5rem; height: 1.5rem;"
                                  onclick="goodsPlus('${params.openid}', ${cart.goodsInfo?.id});"></span>
                        </div>
                    </div>
                </div>
            </li>
            </g:each>
        </ul>
    </div>
    <br /><br />
    <!-- 底部选项卡 -->
    <div class="toolbar cart-bar">
        <div style="margin-top: 0.9rem;">
            <span class="cart-label">总共：</span>
            <span id="cartMoney" style="color: red">0元</span>
            <span class="cart-label">去支付</span>
        </div>

    </div>
</div>
</body>
<asset:javascript src="framework7.min.js"/>
<asset:javascript src="my-app.js"/>
</html>