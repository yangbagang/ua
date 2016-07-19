require.config({
	paths: {
		"jquery": "lib/jquery-1.11.3.min",
		"zepto": "plugins/zepto",
		 "mui" : "plugins/mui.min"
	}
});
var windowH;
require(["jquery","mui"],function($){
	windowH= $(window).height();
	var index = require('main');
	index.getGoodsInfo();
	index.init();
	// 排序字母点击事件
	$(".indexlist").on("tap","li",function() {
		$(this).addClass("active").siblings().removeClass("active");
		var H = $(this).html();
		var self = document.querySelector(".p_choose");

		$(".indexlist_alert").html(H);
		$(".indexlist_alert").show();
		setTimeout(function() {
			$(".indexlist_alert").hide();
		}, 500);
		var L = document.querySelector("[data-group='" + H + "']");
		if (!L) {
			return false;
		}
		self.scrollLeft = L.offsetLeft;
	});
	// 搜索
	$(".search_con").on("input", function() {
		$(".p_choose").children().toggleClass("none");
		if (!$(".indexlist").hasClass("none")) { 
            $(".indexlist").css("display", "table"); 
         } else { 
            $(".indexlist").css("display", "none"); 
         }
		var Sl = $(".result_wrap").find("li").length;
		var Sn = Math.ceil(Sl / 4);
		var H = $(window).height();
		$(".result_wrap").find("li").css("border-bottom", "1px solid #eee");
		$(".result_wrap").find("li:nth-child(4n)").css("border-bottom", "0");
		$(".result_wrap").css({
			"height": (H - 14),
			"width": (100 * Sn)
		});
		getGoodsInfo();
		console.info("OK");
	});
	// 页面移动
	$(".p_choose").on("scroll", function() {
		var Sl = this.scrollLeft;

		$(".data_group").each(function() {
			var Dh = $(this).html();
			var Dl = document.querySelector("[data-group='" + Dh + "']").offsetLeft;

			if (Sl >= Dl) {
				var H = document.querySelector("[data-group='" + Dh + "']").innerHTML;
				$(".indexlist li").each(function() {
					if ($(this).html() == H) {
						$(".indexlist_alert").html(H);
						$(".indexlist_alert").show();
						setTimeout(function() {
							$(".indexlist_alert").hide();
						}, 500);
						$(this).addClass("active").siblings().removeClass("active");
					}
				});
			}
		});
	});
});
var aa = define(['jquery'], function(jquery){
	function getGoodsInfo(){
		$(".p_lists").html("");
		var keyWord = document.getElementById('keyWord').value;
		mui.post('queryGoodsInfo1',{
			token:qtoken,
			keyWord:keyWord,
			goodsInitials:""
		},function(data){
			if(data.success == true){
				var dataList = data.dataList;
				var str= "";
				var j = $("#counts").val();
				for(var i=0;i<dataList.length;i++){
					var cellValue = dataList[i];
					var goodsInitials = cellValue.goodsInitials;
					$("#tihuan0").find("li").attr("data",cellValue.id);
					$("#tihuan0").find(".gray").html(cellValue.goodsName.replace(new RegExp(" ", 'g'), ""));
					$("#tihuan0").find(".red").html("￥"+cellValue.price);
					str = $("#tihuan0").html();
					var obj = $("[data-group='" + goodsInitials + "']");
					if(keyWord != null && keyWord != ""){
						$(".result_wrap").show();
						$(".p_wrap").hide();
						$(".indexlist").hide();
						$(".indexlist_alert").hide();
						$("#resultShow").append(str);
					    var Sl = $(".result_wrap").find("li").length;
						var Sn = Math.ceil(Sl / 4);
						$(".result_wrap").find("li").css("border-bottom", "1px solid #eee");
						$(".result_wrap").find("li:nth-child(4n)").css("border-bottom", "0");
						$(".result_wrap").css({
							"height": (windowH - 14),
							"width": (100 * Sn)
						}); 
					}else{
						obj.removeClass("none");
						$(".p_wrap").show();
						$(".indexlist").css("display", "table");
						$(".result_wrap").hide();
						$(obj).next("ul").append(str);
					}
				}
				$(".p_choose").removeClass("none");
				// 计算列表的宽高
				var A = 0;
				$(".p_wrap .p_lists").each(function() {
					var L = $(this).find("li").length;
					if (L == 0) { 
						   $(this).prev().addClass("none"); 
					 }
					var N = Math.ceil(L / 4);
					$(this).css("width", (100 * N ));
					$(this).find("li").css("border-bottom", "1px solid #eee");
					$(this).find("li:nth-child(4n)").css("border-bottom", "0");
					A += $(this).width();
				});
				
				var I = ($(".p_wrap .data_group").length - $(".p_wrap .data_group.none").length) * 30;
				$(".p_wrap").css({
					"height": (windowH - 44),
					"width": (A + I)
				});
				$(".data_group").css("line-height", (windowH - 44 - 30) + "px");
				
				$("#counts").val(j);
				//document.getElementById('showData').innerHTML = str;
			}else{
				alert(data.message);
			}
			
		},'json'
		);
	};
	function init(){
		mui.post('queryAllGoodsInitials',{
			token:qtoken
		},function(data){
			$(".indexlist").html("");
			var flag = false;
			for(var i = 0;i< data.dataList.length;i++){
				var cellValue = data.dataList[i];
				if(cellValue == "#"){
					flag = true;
				}else{
					if(i == 1){
						$(".indexlist").append("<li class='active'>" + cellValue +"</li>");
					}else{
						$(".indexlist").append("<li>" + cellValue +"</li>");
					}
				}
			}
			if(flag){
				$(".indexlist").append("<li>#</li>");
			}
		},'json'
		);
	};
	function selectGoods(obj){
		var id = $(obj).attr("data");
		window.location.href="managementGoods?token=" + qtoken + "&machineId=" + qmachineId + "&layerIds=" + qlayerIds + "&latitude=" + qlatitude + "&longitude=" + qlongitude + "&index=" + qindex + "&goodsId="+id;
	}
	return {
		getGoodsInfo : getGoodsInfo,
		init:init,
		selectGoods:selectGoods
	};
});