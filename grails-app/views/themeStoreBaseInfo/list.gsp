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
        <div class="content-block-title">所有主题店</div>
        <div class="list-block media-list">
            <ul>
                <g:each var="store" in="${stores}">
                    <li onclick="showStoreGoods(${store.id}, '${params.openid}');"><a href="#" class="item-link item-content">
                        <div class="item-inner">
                            <div class="item-title-row">
                                <div class="item-title">${store.name}</div>
                            </div>
                            <div class="item-subtitle">${store.position}</div>
                        </div></a></li>
                </g:each>
            </ul>
        </div>
    </div>
</div>
</body>
<asset:javascript src="framework7.min.js"/>
<asset:javascript src="my-app.js"/>
</html>