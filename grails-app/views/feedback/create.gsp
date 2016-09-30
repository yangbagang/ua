<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>故障反馈</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <asset:stylesheet src="framework7.material.min.css" />
    <asset:stylesheet src="framework7.material.colors.min.css" />
    <asset:stylesheet src="wx.css" />
</head>
<body>
<div class="page-content">
    <div class="content-block">
        <g:form action="save" method="post">
            <input type="hidden" name="openid" value="${params.openid}">
            <div class="content-block-title">故障反馈单</div>
            <div class="list-block inputs-list">
                <ul>
                    <li>
                        <div class="item-content">
                            <div class="item-inner">
                                <div class="item-title floating-label">设备编号</div>
                                <div class="item-input item-input-field">
                                    <input type="text" name="vmCode" placeholder="" onkeyup="this.value=this.value.toUpperCase()" onafterpaste="this.value=this.value.toUpperCase()"/>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="item-content">
                            <div class="item-inner">
                                <div class="item-title floating-label">类型</div>
                                <div class="item-input item-input-field">
                                    <select name="type">
                                        <option>  </option>
                                        <option value="出货故障">出货故障</option>
                                        <option value="无法支付">无法支付</option>
                                        <option value="无法购买">无法购买</option>
                                        <option value="设备损坏">设备损坏</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="align-top">
                        <div class="item-content">
                            <div class="item-inner">
                                <div class="item-title floating-label">详情</div>
                                <div class="item-input item-input-field">
                                    <textarea name="content" class="resizable"></textarea>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <input type="submit" value="提交" class="button button-raised button-fill color-teal ripple-orange">
        </g:form>
    </div>
</div>
</body>
<asset:javascript src="framework7.min.js" />
<asset:javascript src="my-app.js" />
</html>