<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>绑定手机号</title>
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
        <g:if test="${flash.message != null && flash.message != ''}">
            <div class="color-red">${flash.message}</div>
        </g:if>
        <h4>请输入手机号码</h4>
        <g:form action="checkWXPhone" method="post">
            <input type="hidden" name="openid" value="${params.openid}">
            <div class="form_group">
                <input name="phone" type="tel" autofocus="autofocus" maxlength="11" placeholder="请输入手机号" id="autoFocus" pattern="[0-9]*" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
            </div>
            <br/><br/>
            <div class="row">
                <div class="col-33">
                <input type="submit" class="button button-fill button-green" value="下一步">
                </div>
            </div>
        </g:form>
        <div class="form_group tc">
            <p class="f12">点击“下一步”按钮代表您已阅读并同意</p>
            <a href="${createLink(controller: "userBaseInfo", action: "agreement")}">《平台服务协议》</a>
        </div>
    </div>
</div>
</body>

</html>