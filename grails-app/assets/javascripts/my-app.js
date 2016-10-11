// Initialize your app
var basePath = "http://localhost:8080/ua/";
var myApp = new Framework7({
    material: true,
    showBarsOnPageScrollEnd: false,
    // Hide and show indicator during ajax requests
    onAjaxStart: function (xhr) {
        myApp.showIndicator();
    },
    onAjaxComplete: function (xhr) {
        myApp.hideIndicator();
    }
});

// Export selectors engine
var $$ = Dom7;

$$.postJSON = function(url, data, success) {
    return $$.ajax({
        url: url,
        method: 'POST',
        data: typeof data === 'function' ? undefined : data,
        success: typeof data === 'function' ? data : success,
        dataType: 'json'
    });
}

function showFeedbackDetail(id, openid) {
    var url = basePath + "feedback/show/" + id + "?openid=" + openid;
    window.location.href = url;
}

function showStoreGoods(id, openid) {
    var url = basePath + "vendLayerTrackGoods/listStoreGoods?storeId=" + id + "&openid=" + openid;
    window.location.href = url;
}

function buySingleGoods(openid, goodsId) {
    var url = basePath + "vendLayerTrackGoods/listStoreGoods?storeId=" + id + "&openid=" + openid;
    window.location.href = url;
}

function addToCart(openid, goodsId) {
    var url = basePath + "shoppingCart/addGoods";
    var params = {goodsId: goodsId, openid: openid};
    $$.postJSON(url, params, function(data){
        if (data.success) {
            //成功添加
            myApp.alert("添加成功", "提示");
        } else {
            myApp.alert(data.message, "提示");
        }
    });
}