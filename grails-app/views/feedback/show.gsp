<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>故障反馈</title>
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
        <div class="content-block-title">故障反馈单</div>
        <div class="list-block inputs-list">
            <ul>
                <li>
                    <div class="item-content">
                        <div class="item-inner not-empty-state">
                            <div class="item-title floating-label">提交时间</div>
                            <div class="item-input item-input-field">
                                <input type="text" name="createTime" value="<g:formatDate format="yyyy-MM-dd HH:mm:ss" date="${feedback.createTime}"/>" readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="item-content">
                        <div class="item-inner not-empty-state">
                            <div class="item-title floating-label">设备编号</div>
                            <div class="item-input item-input-field">
                                <input type="text" name="vmCode" value="${feedback.vmCode}" readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="item-content">
                        <div class="item-inner not-empty-state">
                            <div class="item-title floating-label">类型</div>
                            <div class="item-input item-input-field">
                                <input type="text" name="type" value="${feedback.type}"  readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                </li>
                <li class="align-top">
                    <div class="item-content">
                        <div class="item-inner not-empty-state">
                            <div class="item-title floating-label">详情</div>
                            <div class="item-input item-input-field">
                                <textarea name="content" class="resizable"  readonly="readonly">${feedback.content}</textarea>
                            </div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="item-content">
                        <div class="item-inner not-empty-state">
                            <div class="item-title floating-label">处理时间</div>
                            <div class="item-input item-input-field">
                                <input type="text" name="updateTime" value="<g:formatDate format="yyyy-MM-dd HH:mm:ss" date="${feedback.updateTime}"/>"  readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="item-content">
                        <div class="item-inner not-empty-state">
                            <div class="item-title floating-label">处理方式</div>
                            <div class="item-input item-input-field">
                                <input type="text" name="result" value="${feedback.result}"  readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="item-content">
                        <div class="item-inner not-empty-state">
                            <div class="item-title floating-label">当前状态</div>
                            <div class="item-input item-input-field">
                                <input type="text" name="flag" value="${feedback.flag == 1 ? '己处理' : '未处理'}"  readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
<asset:javascript src="framework7.min.js"/>
<asset:javascript src="my-app.js"/>
</html>