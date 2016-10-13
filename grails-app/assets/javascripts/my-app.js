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
            getGoodsNumAndMoney(openid);
        } else {
            myApp.alert(data.message, "提示");
        }
    });
}

function getGoodsNumAndMoney(openid) {
    var url = basePath + "shoppingCart/getGoodsNumAndMoney";
    var params = {openid: openid};
    $$.postJSON(url, params, function(data){
        if (data.success) {
            //成功
            $$("#cartNum").html(data.num);
        } else {
            myApp.alert(data.message, "提示");
        }
    });
}

function showCart(storeId, openid) {
    var url = basePath + "shoppingCart/showCart?openid=" + openid + "&storeId=" + storeId;
    window.location.href = url;
}

function sumCart() {
    var sum = 0;
    $$(".cart-item").each(function (index, el) {
        var cartChecked = $$(el).find("input")[0];
        var isChecked = $$(cartChecked).prop('checked');
        if (isChecked) {
            var num = $$(el).data("num");
            var price = $$(el).data("price");
            sum += num * price;
        }
    });
    $$("#cartMoney").html(sum + "元");
}

function removeGoods(openid, goodsId) {
    var url = basePath + "shoppingCart/deleteGoods";
    var params = {openid: openid, goodsId: goodsId};
    $$.postJSON(url, params, function(data){
        if (data.success) {
            //成功
            var cartItem = $$("#item-" + goodsId);
            $$(cartItem).remove();
        } else {
            myApp.alert(data.message, "提示");
        }
    });
}

function goodsPlus(openid, goodsId) {
    var url = basePath + "shoppingCart/addGoods";
    var params = {openid: openid, goodsId: goodsId};
    $$.postJSON(url, params, function(data){
        if (data.success) {
            //成功
            goodsNumAdd(1, goodsId);
        } else {
            myApp.alert(data.message, "提示");
        }
    });
}

function goodsMinus(openid, goodsId) {
    var url = basePath + "shoppingCart/removeGoods";
    var params = {openid: openid, goodsId: goodsId};
    $$.postJSON(url, params, function(data){
        if (data.success) {
            //成功
            goodsNumAdd(-1, goodsId);
        } else {
            myApp.alert(data.message, "提示");
        }
    });
}

function goodsNumAdd(n, goodsId) {
    var cartItem = $$("#item-" + goodsId)[0];
    var num = eval($$(cartItem).data("num"));
    var price = eval($$(cartItem).data("price"));
    num += n;
    if (num == 0) {
        $$(cartItem).remove();
    } else {
        $$(cartItem).data("num", num);
        $$(cartItem).find(".price").html("￥" + (price * num));
        $$(cartItem).find(".money").html("" + price + "*" + num);
        $$(cartItem).find(".goods-num").html(num);
    }
    sumCart();
}

function checkItem(goodsId) {
    var cartItem = $$("#item-" + goodsId)[0];
    var cartChecked = $$(cartItem).find("input")[0];
    var isChecked = $$(cartChecked).prop('checked');
    $$(cartChecked).prop('checked', !isChecked);
    sumCart();
}