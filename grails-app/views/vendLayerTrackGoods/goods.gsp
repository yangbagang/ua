<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>主题店</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <asset:stylesheet src="framework7.material.min.css"/>
    <asset:stylesheet src="framework7.material.colors.min.css"/>
    <asset:stylesheet src="wx.css"/>
</head>

<body>
<div class="page-content">
    <div class="content-block">
        <div class="content-block-title">所有商品</div>
        <div class="list-block media-list">
            <ul>
                <g:each var="g" in="${goods}">
                    <li><a href="#" class="item-link item-content">
                        <div class="item-inner">
                            <div class="item-title">${g[2].name}</div>
                            <div class="item-subtitle">
                                规格：${g[2].specifications}，单价：${g[2].realPrice}元。<br/>
                            </div>
                        </div></a>
                        <p class="buttons-row">
                            <input type="button" class="button button-raised button-fill color-cyan ripple-pink" value="加入清单" onclick="addToCart('${params.openid}', ${g[2].id});">
                            <input type="button" class="button button-raised button-fill color-teal ripple-yellow" value="单件购买" onclick="buySingleGoods('${params.openid}', ${g[2].id});">
                        </p>
                    </li>
                </g:each>
            </ul>
        </div>
    </div>
</div>
</body>
<asset:javascript src="framework7.min.js"/>
<asset:javascript src="my-app.js"/>
</html>