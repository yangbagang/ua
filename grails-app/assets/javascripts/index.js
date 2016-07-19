require.config({
	paths: {
		"jquery": "lib/jquery-1.11.3.min",
		 "mui" : "plugins/mui.min",
		 "swiper": "plugins/swiper.min",
		 "dialog": "plugins/dialog",
	}
});
var mySwiper;
require(["jquery","mui","swiper"],function($){
	Array.prototype.max=function(){
		var max = this[0];
		for(var i=1;i<this.length;i++){ 
		  if(max<this[i])max=this[i];
		}
		return max;
	}
	mySwiper = new Swiper('#p_swipe', {
		pagination: '.page_dot', // 分页圆点
		noSwiping: true, // 禁止swiper
		noSwipingClass: "stop-swiping", // 禁止swiper类名
		mode: 'horizontal', // swiper方向
		threshold: 50, // swiper拖动位置
		touchRatio: 0.5,
		paginationClickable: true, // 分页圆点点击, 
	    onSlideChangeEnd: function(swiper){ 
	  	  var index = swiper.activeIndex;
	  	  if(index > 0){
	  		  var data = $("#slider").children().eq(index).attr("data");
	  		  var data1 = $("#slider").children().eq(index).attr("data1");
	  		  var id = data.split("#")[0];
	  		  var num = data.split("#")[1];
	  		  if(data1 == 0){
	  			  getSubData(id,num,index,0);
		    	  }
	  		}
	    } 
	});
		// 上下滑动显示隐藏顶部标题栏
		$('#p_swipe').on("touchstart", ".p_wrap", function(e) {
				var touch = e.originalEvent.targetTouches[0];
				startPosition = {
					y: touch.pageY
				}
			}).on("touchmove", ".p_wrap", function(e) {
				var touch = e.originalEvent.targetTouches[0];
				endPosition = {
					y: touch.pageY
				}
				deltaY = endPosition.y - startPosition.y;
				var offTop = 44;
				var scrTop = $(this).scrollTop();
				if (deltaY < 0 && scrTop >= offTop) {
					$(this).closest(".page").find("header.mui-bar").hide();
					$(".page_dot").hide();
				} else {
					$(this).closest(".page").find("header.mui-bar").show();
					$(".page_dot").show();
				}
		});

		// 显示隐藏右侧操作按钮
		$('#p_swipe').on("tap", ".show_opt", function() {
			$(this).closest(".page").find(".setting").toggleClass("none");
		});

		// 调换位置
		var pHtml = "";
		$('#p_swipe').on("tap", ".page .btn_order", function() {
			pHtml = $(this).closest(".page").find(".p_wrap").html();

			$(this).closest(".page").find(".choosed_wrap").addClass("none");
			$(this).closest(".page").find(".setting").addClass("none");
			$(this).closest(".page").addClass("stop-swiping");
			
			$(this).closest(".page").find(".mui-icon-left-nav").addClass("none").siblings(".cancel_opt").removeClass("none");
			$(this).closest(".page").find(".show_opt").addClass("mui-icon mui-icon-checkmarkempty save_opt").removeClass("show_opt").html("<span id='saveInfo' onclick='saveInfo()'>保存</span>");

			$(this).closest(".page").find(".p_content").each(function() {
				$(this).removeClass("checked default");
				$(this).addClass("shake");
			});

		});
		// 取消调换位置
		$('#p_swipe').on("tap", ".page .cancel_opt", function() {
			$(this).closest(".page").find(".opt_wrap").addClass("none");
			var checkIndex = $(this).closest(".page").find(".checked").length;
			if (checkIndex >= 1) {
				$(this).closest('.page').find(".choosed_wrap").removeClass("none");
			} else {
				$(this).closest('.page').find(".choosed_wrap").addClass("none");
			}
			$(this).closest(".page").find(".p_wrap").css("padding-bottom", "0");
			$(this).closest(".page").removeClass("stop-swiping");
			$(this).closest(".page").find(".p_wrap").html(pHtml);
			$(".save_p").html("");
			$(this).closest(".page").find(".mui-icon-left-nav").removeClass("none").siblings(".cancel_opt").addClass("none");
			$(this).closest(".page").find(".save_opt").addClass("show_opt").removeClass("mui-icon mui-icon-checkmarkempty save_opt").html("<span class='g_icon g_more'></span>");
		});

		// 点击抖动商品事件
		var L = "";
		$('#p_swipe').on("tap", ".p_wrap .shake", function() {
			$(this).closest(".page").find(".opt_wrap").removeClass("none");
			$(this).closest(".page").find(".save_wrap").children().removeClass("none");
			$(this).closest(".page").find(".p_wrap").css("padding-bottom", "80px");
			var saveHtml = $(this).closest(".page").find(".save_p").html();
			var pShow = $(this).closest(".p_content").html();
			$(this).removeClass("shake").html("");
			saveHtml += ('<li class="p_info"><div class="p_content">' + pShow +'</div></li>');
			$(this).closest(".page").find(".save_p").html(saveHtml);
			$(".save_p").removeClass("shake");
			
			L = $(".save_p").find("li").length;
			var W = $(".save_p").find("li").width() + 15;
			$(".save_p").css("width", (W * L));
		});

		// 点击已选底部商品事件
		$('#p_swipe').on("tap", ".save_p .p_content", function() {
			var showHtml = $(this).html();
			$.each($(this).closest(".page").find(".p_content"), function() {
				if (!$(this).text()) {
					$(this).html(showHtml);
					$(this).addClass("shake");
					return false;
				}
			});
			$(this).parent("li").remove();
			
			L = $(".save_p").find("li").length;
			var W = $(".save_p").find("li").width() + 15;
			$(".save_p").css("width", (W * L));
		});
		
		// 商品选中状态
		// 列
		$('#p_swipe').on("tap", ".p_wrap .p_col", function() {
			var index = require('index');
			index.selectCol(this);
		});
		// 行
		$('#p_swipe').on("tap", ".p_wrap .p_row", function() {
			var index = require('index');
			index.selectRow(this);
		});
		// 全选
		$('#p_swipe').on("tap", ".p_wrap .p_all", function() {
			var index = require('index');
			index.selectAll(this);
		});
		// 单个商品
		$('#p_swipe').on("tap", ".p_wrap .p_info", function() {
			var index = require('index');
			index.clickSingle(this);
		});
		mui(".set_num").on("tap", "li", function() {
			$(".set_num").find("li").removeClass("active");
			$(this).toggleClass("active");
		});
		// 关闭弹窗
		$('#p_swipe').on("tap",".cancel, .confirm", function() {
			$(this).closest(".mui-backdrop").addClass("none");
		});
		// 设定价格
		$('#p_swipe').on('tap',".set_price", function(e) {
			var index = require('index');
			index.beforeSetPrice();
		});
		//设置价格按钮
		$('#p_swipe').on("tap",".confirm_price", function() {
			var index = require('index');
			index.savePrice(this);
		});
		$(".price_dialog").on("keyup", function(e) {
			if (e.keyCode == 13) {
				var priceVal = $(this).find("input").val();
				$(this).closest(".mui-backdrop").addClass("none");
			}
		});
		// 设定数量
		$(".setting_num").on('tap', function(e) {
			var index = require('index');
			index.beforeSetNum();
		});
		$(".set_num li").on("tap", function() {
			$(this).toggleClass("active");
		});
		$(".confirm_num").on("tap", function() {
			var index = require('index');
			index.setNum();
		});
		// 跳转页面
		$('.chooseProduct').on('tap', function(){
			var index = require('index');
			index.chooseGoods();
		});
		// 选择商品
		$('#p_swipe').on("tap", ".gezi", function() {
			var index = require('index');
			index.chooseGZGoods();
		});
		// 格子柜设置数量
		$('#p_swipe').on("tap", ".setGnum", function() {
			var index = require('index');
			index.setGnum(this);
		});
		// 开空柜门
		$('#p_swipe').on("tap", ".open_empty", function() {
			var index = require('index');
			index.openEmptyDoor();
		});
});
var aa = define(['jquery','swiper','mui','dialog'], function(jquery){
	function saveInfo(){//用来保存调换位置的信息
		var str = "";
		var index = mySwiper.activeIndex;
		var obj = "showData";
		if(index != 0){
			obj = "showG";
		}
		 $("#slider").children().eq(index).find("#"+obj).find(".p_info").each(function() {
			//存放商品ID,层级ID,层数,轨道编号
			var data = $(this).attr("data");
			var data1 = $(this).find("#goodsName").attr("data");
			if(data != data1){
				var ss = data+"#"+data1;
				str += ss+",";
			}
		});
		if($("#"+obj).closest('.page').find(".save_p").html() !=""){
			dialog._alert("icon-notice","请完成操作再保存");
			return;
		 }
		if(str != ""){
			mui.post('savePosition',{
				token:qtoken,
				datas:str,
				machineId:qmachineId
			},function(data){
				if(data.success == true){
					dialog._alert("icon-check","保存成功");
					if(index == 0){
						init(true,1);
					}else{
						var obj = $("#slider").children().eq(index);
						var datas = obj.attr("data");
						var id = datas.split("#")[0];
			    		var num = datas.split("#")[1];
						 getSubData(id,num,index,1);
					}
				}else{
					alert(data.message);
				}
			},'json'
			);
		 }
		$('#slider').children().eq(index).find(".save_opt").addClass("show_opt").removeClass("mui-icon mui-icon-checkmarkempty save_opt").html("<span class='g_icon g_more'></span>");
		$('#slider').children().eq(index).find(".opt_wrap").addClass("none");
		$("#slider").children().eq(index).find("#"+obj).css("padding-bottom", "0");
		$("#slider").children().eq(index).removeClass("stop-swiping");
		$("#slider").children().eq(index).find(".shake").removeClass("shake");
		$("#slider").children().eq(index).find(".mui-icon-left-nav").removeClass("none").siblings(".cancel_opt").addClass("none");;

	};
	function init(flag,isFirst){//初始化数据 加载主机数据
		//var shtml = $("#zhujiHeader").html();
		$("#showData").html("");
		//$("#showData").append(shtml);
		$("#tihuan").find("#kucun").show();
		$("#tihuan0").find("#kucunTitle").show();
		var layerIds = $("#layerIds").val();
		if(isFirst != 0){
			layerIds = "";
		}
		mui.post('getGoodsInfo',{
			token:qtoken,
			machineId:qmachineId
		},function(data){
			if(data.success == true && data.status == "000"){
				//debugger;
				var str= "";
				//var layerIds = $("#layerIds").val();
				var layerArray = new Array();
				if(layerIds != null && layerIds !=""){
					layerArray = layerIds.split(",");
				}
				var layerNum = data.layerNum;
				var layerOrbitalArray = data.layerOrbitalNum;
				var maxNum = layerOrbitalArray.max();
				$("#showData").append($("#zhujiHeader" + maxNum).html());
				var info = data.subList;
				if(info != null && info !="" && !flag){
					$("#layerNum").val(info.length);
					var str1 = $("#str").val();
					var array = new Array();
					if(str1 !="" && str1!=null){
						array = str1.split(";");
					}
					for(var i=0;i<info.length;i++){
						var gnum = info[i].num;
						if(gnum == 88){
							$("#g08li").find("#g08").attr("data",info[i].id+"#"+gnum);
							$("#g08li").find("#g08").attr("data1",0);//判断是否已加载数据
							$("#g08li").find(".mui-title").html(info[i].name);
							$("#g08li").find(".glayer").attr("id","glayer"+(i+1));
							$("#g08li").find(".glayer").attr("value",array[i]);
							//$("#g08li").find(".chooseProduct").attr("onclick","gChooseProduct()");
							mySwiper.appendSlide($("#g08li").html());
						}else if(gnum == 50){
							$("#g05li").find("#g05").attr("data",info[i].id+"#"+gnum);
							$("#g05li").find("#g05").attr("data1",0);//判断是否已加载数据
							//$("#g05li").find(".chooseProduct").attr("onclick","gChooseProduct()");
							$("#g05li").find(".mui-title").html(info[i].name);
							$("#g05li").find(".glayer").attr("id","glayer"+(i+1));
							$("#g05li").find(".glayer").attr("value",array[i]);
							mySwiper.appendSlide($("#g05li").html());
						}else if(gnum == 64){
							$("#g0804li").find("#g0804").attr("data",info[i].id+"#"+gnum);
							$("#g0804li").find("#g0804").attr("data1",0);//判断是否已加载数据
						//	$("#g0804li").find(".chooseProduct").attr("onclick","gChooseProduct()");
							$("#g0804li").find(".mui-title").html(info[i].name);
							$("#g0804li").find(".glayer").attr("id","glayer"+(i+1));
							$("#g0804li").find(".glayer").attr("value",array[i]);
							mySwiper.appendSlide($("#g0804li").html());
						}
					}
				}
				var h= 0;
				var k = 0;
				for(var i=0;i<layerNum;i++){
					if(layerOrbitalArray[i] == 10){
						str += "<ul class='p_lists p_10' data=" + (layerOrbitalArray[i] -i)+">";
					}else if(layerOrbitalArray[i] == 9){
						str += "<ul class='p_lists p_09' data=" + (layerOrbitalArray[i] -i)+">";
					}else if(layerOrbitalArray[i] == 8){
						str += "<ul class='p_lists p_08' data=" + (layerOrbitalArray[i] -i)+">";
					}else if(layerOrbitalArray[i] == 5){
						str += "<ul class='p_lists p_05' data=" + (layerOrbitalArray[i] -i)+">";
					}
					$("#tihuan0").find("#showTitle").html(data.layerNums[i]);
					k ++ ;
					str += $("#tihuan0").html();
					var dataList =  data.dataList[i];
					for(var j=0;j<layerOrbitalArray[i];j++){
						if(j< dataList.length){
							var goodsName = "暂无商品";
							var goodsPic= '<%=basePath %>img/no_img.png';
							if(dataList[j].id != null && dataList[j].id != ""){
								goodsName  = dataList[j].goodsName;
								goodsPic = dataList[j].goodsPic;
							}
							if(goodsPic == "" || goodsPic ==null){
								goodsPic= '<%=basePath %>img/no_img.png';
							}
							//存放商品ID,层级ID,层数,轨道编号
							var datas = dataList[j].id+";"+dataList[j].lid;
							for(var v=0;v<layerArray.length;v++){
								if(layerArray[v] == dataList[j].lid){
									$("#tihuan").find("li").addClass("checked");
								}
							}
							if(dataList[j].currentInventory == 0){
								$("#tihuan").find("#cunshu").addClass("default");
							}else{
								$("#tihuan").find("#cunshu").removeClass("default");
							}
							$("#tihuan").find("h3").html(dataList[j].orbitalNo);
							$("#tihuan").find("li").attr("data-group",j+1);
							$("#tihuan").find("#cunshu").attr("data",datas);
							$("#tihuan").find("#cunshu").attr("data_layer",dataList[j].layer);
							$("#tihuan").find("#goodsPic").attr("src",goodsPic);
							$("#tihuan").find("#goodsName").html(goodsName.replace(new RegExp(" ", 'g'), ""));
							$("#tihuan").find("#goodsName").attr("data",datas);
							$("#tihuan").find("#kucun").attr("data",dataList[j].lid);
							$("#tihuan").find("#kucun").html(dataList[j].currentInventory+"/"+dataList[j].largestInventory);
							$("#tihuan").find("#price").html(dataList[j].standardPrice);
							$("#tihuan").find("#cunshu").attr("datas",dataList[j]);
							str += $("#tihuan").html();
							$("#tihuan").find("li").removeClass("checked");	
						}else{
							str += "<li class='p_info' id='cunshu'><h3></h3>";
							str += "<div class='p_content'>";
							str += "</div></li>";
						}
					}
					str += "</ul>";
				}
				//document.getElementById('showData').innerHTML = str;
				$("#showData").append(str);
			}else if(data.status == "001"){
				dialog._alert("icon-cancel",data.message);
			}else if(data.status == "002"){
				alert(data.message);
			}
			var index = qindex;
			if(index != null && flag == false){
				mySwiper.slideTo(index, 1000, true);
			}
		},'json'
		);
	};
	function isSelect(obj,flag,select){//选中事件
		if(flag == 0){//全选
			if(select == 1){
				$(obj).closest('.page').find(".choosed_wrap").removeClass("none");
				var Ch = $(obj).closest('.page').find(".choosed_wrap").height();
				$(obj).closest(".page").find(".p_wrap").css("padding-bottom", Ch);
				$(obj).closest('.page').find(".choosed_wrap").html("<span class='gray'>已选轨道: </span><span class='choosed_rail'>全选</span>");
			}else{
				$(obj).closest('.page').find(".choosed_wrap").addClass("none");
				$(obj).closest(".page").find(".p_wrap").css("padding-bottom", 0);
				$(obj).closest('.page').find(".choosed_wrap").html("");
			}
		}else{
			var rowstr = "";
			var colstr = "";
			var obNum = "";
			$(obj).closest(".p_wrap").find(".p_row").each(function(){
				var index = $(this).parent().index();
				if($(this).hasClass("checked")){
					rowstr += index+",";
				}else{
					$(obj).closest(".p_wrap").find("ul").eq(index).find(".p_info.checked").each(function(){
						var num = $(this).attr("data-group");
						if($(this).closest(".p_wrap").find(".p_num").find("li").eq(num).hasClass("checked")){
						
						}else{
							obNum +=$(this).find("h3").html()+",";
						}
						
					})
				}
			})
			$(obj).closest(".p_wrap").find(".p_col").each(function(){
			var index = $(this).index();
			if($(this).hasClass("checked")){
				colstr += index+",";
			}else{
				 $(this).closest(".p_wrap").find("[data-group='" + index + "']").each(function(){
					 if($(this).hasClass("checked")){
						 var num = $(this).parent().index();
						 if(!$(obj).closest(".p_wrap").find("ul").eq(num).find("li").eq(0).hasClass("checked")){
							 obNum +=$(this).find("h3").html()+",";
						 }
					 }
				 });
			}
			})
			var array = obNum.substring(0,obNum.length-1).split(",");
			var array1 = unique(array);
			var checkIndex = $(obj).closest(".page").find(".p_info.checked").length;
			var count = 0;
			if (checkIndex >= 1) {
				var string = "";
				$(this).closest('.page').find(".choosed_wrap").removeClass("none");
				var Ch = $(this).closest('.page').find(".choosed_wrap").height();
				$(this).closest(".page").find(".p_wrap").css("padding-bottom", Ch);
				if(rowstr != ""){
					var temp1 = rowstr.substring(0,rowstr.length-1).split(",");
					for(var i=0;i<temp1.length;i++){
						count ++;
						string += "<span class='choosed_rail'>第"+ temp1[i] +"层</span>"
					}
				}
				if(colstr != ""){
					var temp2 = colstr.substring(0,colstr.length-1).split(",");
					for(var i=0;i<temp2.length;i++){
						count ++;
						string += "<span class='choosed_rail'>第"+ temp2[i] +"列</span>"
					}
				}
				if(array1 != "" && array1.length>0){
					for(var i=0;i<array1.length;i++){
						count ++;
						string += "<span class='choosed_rail'>"+ array1[i] +"</span>"
					}
				}
				if($(obj).closest(".p_wrap").find(".p_all").hasClass("checked")){
					count == 0
					string = "<span class='choosed_rail'>全选</span>";
				}
				if(count >15){
					string = "<span class='choosed_rail'>已选择" + count + "个</span>";
				}
				$(obj).closest('.page').find(".choosed_wrap").html("<span class='gray'>已选轨道: </span>").append(string);
			} else {
				$(obj).closest('.page').find(".choosed_wrap").addClass("none");
				$(obj).closest(".page").find(".p_wrap").css("padding-bottom", 0);
				$(obj).closest('.page').find(".choosed_wrap").html("");
			}
		}
		
	};
	function unique(arr) {//去重复项
	    var result = [], hash = {};
	    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
	        if (!hash[elem]) {
	            result.push(elem);
	            hash[elem] = true;
	        }
	    }
	    return result;
	};
	function getSubData(sid,num,index,flag){//加载格子柜数据
		if(num == 88){
			var html = $("#g08Header").html();
		}else if(num == 50){
			var html = $("#g05Header").html();
		}else{
			var html = $("#g08Header").html();
		}
		var layerIds = $("#layerIds").val();
		if(flag != 0){
			layerIds = "";
		}
		$("#slider").children().eq(index).attr("data1",1);
		$("#slider").children().eq(index).find("#showG").html("");
		var  layerName = $("#slider").children().eq(index).find(".mui-title").html();
		$("#slider").children().eq(index).find("#showG").append(html);
		mui.post('selectSubCabinetGoods',{
			token:qtoken,
			sid:sid
		},function(data){
			if(data.success == true){
				var str= "";
				var str1 = "";
				var str2 = "";
				var dataList = data.dataList;
				var h=0;
				var temp3= 0;
				//var layerIds = $("#layerIds").val();
				var layerArray = new Array();
				if(layerIds != null && layerIds !=""){
					layerArray = layerIds.split(",");
				}
				for(var i=0;i<dataList.length;i++){
					if(num==88 && i%8 == 0){
						str += "<ul class='p_lists'>";
						h++;
						temp3 = 0;
						$("#tihuan0").find("#showTitle").html(h);
						str1 = "";
						$("#tihuan0").find("#kucunTitle").hide();
						str2 = $("#tihuan0").html();
					}else if(num==50 && i%5 == 0){
						str += "<ul class='p_lists'>";
						h++;
						$("#tihuan0").find("#showTitle").html(h);
						str1 = "";
						$("#tihuan0").find("#kucunTitle").hide();
						str2 = $("#tihuan0").html();
					}else if(num==64){
						if(i<48 && i%8 == 0){
							str += "<ul class='p_lists p_08'>";
							h++;
							temp3 = 0;
							$("#tihuan0").find("#showTitle").html(h);
							str1 = "";
							$("#tihuan0").find("#kucunTitle").hide();
							str2 = $("#tihuan0").html();
						}else if(i>= 48 && i%4 == 0){
							str += "<ul class='p_lists p_04'>";
							h++;
							temp3 = 0;
							$("#tihuan0").find("#showTitle").html(h);
							str1 = "";
							$("#tihuan0").find("#kucunTitle").hide();
							str2 = $("#tihuan0").html();
						}
					}
					var goodsName = "暂无商品";
					if(dataList[i].id != null && dataList[i].id != ""){
						goodsName  = dataList[i].goodsName;
					}
					//存放商品ID,层级ID,层数,轨道编号
					var datas = dataList[i].id+";"+dataList[i].lid;
					for(var v=0;v<layerArray.length;v++){
						if(layerArray[v] == dataList[i].lid){
							$("#tihuan").find("li").addClass("checked");
						}
					}
					if(dataList[i].currentInventory == 0){
						$("#tihuan").find("#cunshu").addClass("default");
					}else{
						$("#tihuan").find("#cunshu").removeClass("default");
					}
					$("#tihuan").find("h3").html(dataList[i].orbitalNo);
					temp3 ++ ;
					if(num == 88){
						$("#tihuan").find("li").attr("data-group",9-temp3);
					}else if(num == 50){
						$("#tihuan").find("li").attr("data-group",6-temp3);
					}else if(num==64){
						if(i<48){
							$("#tihuan").find("li").attr("data-group",9-temp3);
						}else{
							$("#tihuan").find("li").attr("data-group",5-temp3);
						}
					}
					
					$("#tihuan").find("#cunshu").attr("data",datas);
					$("#tihuan").find("#goodsName").html(goodsName.replace(new RegExp(" ", 'g'), ""));
					$("#tihuan").find("#goodsName").attr("data",datas);
					$("#tihuan").find("#kucun").attr("data",dataList[i].lid);
					$("#tihuan").find("#kucun").hide();
					/*$("#tihuan").find("#kucun").html(dataList[i].currentInventory+"/"+dataList[i].largestInventory);*/
					$("#tihuan").find("#price").html(dataList[i].standardPrice);
					$("#tihuan").find("#cunshu").attr("datas",dataList[i].orbitalNo);
					//$("#tihuan").find("#cunshu").attr("onclick","gSelected(this)");
					console.info("-----" + isNaN(layerName) + "----" + layerName);

					if(!isNaN(layerName)){
						str1 += $("#tihuan").html();
					}else{
						str1 = $("#tihuan").html() + str1;
					}
					$("#tihuan").find("li").removeClass("checked");
					if(num==88 && i%8 == 7){
						str += str2 + str1 + "</ul>";
					}else if(num==50 && i%5 == 4){
						str += str2 + str1 + "</ul>";
					}else if(num==64){
						if((i<48 && i%8 == 7) || (i>=48 && i%4 == 3)){
							str += str2 + str1 + "</ul>";
						}
					}
				}
				//document.getElementById('showData').innerHTML = str;
				$("#slider").children().eq(index).find("#showG").append(str);
			}else{
				alert(data.message);
			}

		},'json'
		);
	};
	function openEmptyDoor(){//打开空柜门
		 var index = mySwiper.activeIndex;
		 var obj = $("#slider").children().eq(index);
		 var data = $(obj).attr("data");
		 var sid = data.split("#")[0];
	 	 mui.post('openEmptyDoor',{
				token:qtoken,
				machineId:qmachineId,
				sid:sid
		},function(data){
			if(data.status == "000"){
				$(obj).closest(".page").find(".setting").toggleClass("none");
				dialog._alert("icon-check","指令发送成功");
			}else if(data.status == "001"){
				$(obj).closest(".page").find(".setting").toggleClass("none");
				dialog._alert("icon-cancel",data.message);
			}else if(data.status == "002"){
				alert(data.message);
			}else{
				dialog._alert("icon-cancel",data.message);
			}
		},'json'
		);
	};
	function openDoor(){//开柜门
		var index = mySwiper.activeIndex;
		var obj = $("#slider").children().eq(index);
		var ids = "";
		$(obj).find(".checked").each(function() {
    		if(!$(this).hasClass("p_label")  && !$(this).hasClass("p_col") ){
				var lid = $(this).attr("datas");
				ids += lid+",";
			}
		});
		 if(ids == ""){
			 dialog._alert("icon-notice","请选择格子");
		 }else{
			 dialog._delete("您确定要打开选中格子柜门吗？");
			 $("#sure1").on("tap",function(){
				 var index = mySwiper.activeIndex;
				 var obj = $("#slider").children().eq(index);
				 	 mui.post('openDoor',{
							token:qtoken,
							machineId:qmachineId,
							trackNos:ids
					},function(data){
						if(data.status == "000"){
							$(obj).closest(".page").find(".setting").toggleClass("none");
							dialog._alert("icon-check","指令发送成功");
							$(obj).find("li").each(function() {
					    		$(this).removeClass("checked");
							});
							isSelect(obj,3,0);
						}else if(data.status == "001"){
							$(obj).closest(".page").find(".setting").toggleClass("none");
							dialog._alert("icon-cancel",data.message);
						}else{
							alert(data.message);
						}
					},'json'
					);
			})
		 }
	};
	function setGnum(thisobj){//设置格子柜的数量
		var index = mySwiper.activeIndex;
		var obj = $("#slider").children().eq(index);
		var num = $(thisobj).attr("data");
		var ids = getSelectIds();
		if(ids == ""){
			dialog._alert("icon-notice","请选择格子");
		}else{
			mui.post('saveNum',{
				token:qtoken,
				layerIds:ids,
				num:num
			},function(data){
				if(data.success == true){
					var datas = obj.attr("data");
					var id = datas.split("#")[0];
		    		var num = datas.split("#")[1];
		    		colseChoose("showG");
		    		$(obj).closest(".page").find(".setting").toggleClass("none");
		    		getSubData(id,num,index,1);
				}else{
					alert(data.message);
				}
			},'json'
			);
		}
	};
	function chooseGZGoods(){//格子柜选择商品
		var index = mySwiper.activeIndex;
		var ids = getSelectIds();
		if(ids == ""){
			dialog._alert("icon-notice","请选择格子");
		}else{
			window.location.href = 'chooseGoods?token='+qtoken+'&machineId='+qmachineId+'&latitude='+qlatitude+'&longitude='+qlongitude+'&layerIds='+ids+ '&index='+index;
		}
	};
	function chooseGoods(){
		var ids = getSelectIds();
		if(ids == ""){
			dialog._alert("icon-notice","请选择轨道");
		}else{
			window.location.href = 'chooseGoods?token='+qtoken+'&machineId='+qmachineId+'&latitude='+qlatitude+'&longitude='+qlongitude+'&layerIds='+ ids;
		}
	};
	function colseChoose(obj){
		var index = mySwiper.activeIndex;
		var obj1 = $("#slider").children().eq(index);
		obj1.closest('.page').find(".choosed_wrap").addClass("none");
		obj1.closest(".page").find(".p_wrap").css("padding-bottom", 0);
		obj1.closest('.page').find(".choosed_wrap").html("");
	};
	function setNum(){//主机设置数量
		var ids = getSelectIds();
		var num =$("#setNumSelect").find(".active").html();
		if(num == null){
			dialog._alert("icon-notice","请选择数量");
			return;
		}
		mui.post('saveNum',{
			token:qtoken,
			layerIds:ids,
			num:num
		},function(data){
			if(data.success == true){
				init(true,1);
				colseChoose("showData");
				$("#showData").closest(".page").find(".setting").toggleClass("none");
			}else{
				alert(data.message);
			}
		},'json'
		);
	};
	function beforeSetNum(){
		var ids = getSelectIds();
		if(ids == ""){
			dialog._alert("icon-notice","请选择轨道");
		}else{
			$(".set_num li").removeClass("active");
			$(".num_dialog").removeClass("none");
		}
	};
	function savePrice(thisobj){//设置价格
		var price = $(thisobj).closest(".price_dialog").find("input").val();
		var index = mySwiper.activeIndex;
		var obj = $("#slider").children().eq(index);
		var ids = getSelectIds();
		if(null == price || isNaN(parseFloat(price)) || parseFloat(price)<0){
			dialog._alert("icon-notice","请输入不小于0的数字");
		}else{
			mui.post('savePrice',{
				token:qtoken,
				layerIds:ids,
				price:price
			},function(data){
				if(data.success == true){
					if(index == 0){
						init(true,1);
						colseChoose("showData");
					}else{
						var datas = obj.attr("data");
						var id = datas.split("#")[0];
			    		var num = datas.split("#")[1];
						getSubData(id,num,index,1);
						colseChoose("showG");
					}
					$(obj).closest(".page").find(".setting").toggleClass("none");
				}else{
					alert(data.message);
				}
			},'json'
			);
		}
	};
	function getSelectIds(){
		var index = mySwiper.activeIndex;
		var obj = $("#slider").children().eq(index);
		var ids = "";
		$(obj).find(".checked").each(function() {
    		if(!$(this).hasClass("p_label")  && !$(this).hasClass("p_col") ){
				var lid = $(this).find("#kucun").attr("data");
				ids += lid+",";
			}
		});
		return ids;
	};
	function beforeSetPrice(){
		var ids = getSelectIds();
		if(ids == ""){
			dialog._alert("icon-notice","请选择轨道");
		}else{
			$(".price_dialog").find("input").val("");
			$(".price_dialog").find("input").focus();
			$(".price_dialog").removeClass("none");	
		}
	};
	function clickSingle(thisobj){
		if ($(thisobj).closest(".page").find(".save_opt").html() == null) {
			$(thisobj).toggleClass("checked");
			var checkIndex = $(thisobj).closest(".page").find(".p_info.checked").length;
			if (checkIndex >= 1) {
				$(thisobj).closest('.page').find(".choosed_wrap").removeClass("none");
				var Ch = $(thisobj).closest('.page').find(".choosed_wrap").height();
				$(thisobj).closest(".page").find(".p_wrap").css("padding-bottom", Ch);
			} else {
				$(thisobj).closest('.page').find(".choosed_wrap").addClass("none");
				$(thisobj).closest(".page").find(".p_wrap").css("padding-bottom", 0);
			}
			var index = $(thisobj).index();
			if (!$(thisobj).hasClass("checked")) {
				$(thisobj).siblings(".p_row").removeClass("checked");
				$(thisobj).closest(".p_wrap").find(".p_all").removeClass("checked");
				$(thisobj).closest(".p_wrap").find(".p_col").eq(index - 1).removeClass("checked");
			}

			var Al = $(thisobj).closest(".p_wrap").find(".p_info").length;
			var Rl = $(thisobj).parent(".p_lists").find("li.p_info").length;
			var Rc = $(thisobj).parent(".p_lists").find(".p_info.checked").length;
			var Cl = $(thisobj).closest(".p_wrap").find("[data-group='" + index + "']").length;
			var Cc = $(thisobj).closest(".p_wrap").find("[data-group='" + index + "'].checked").length;
			if (checkIndex == Al) {
				$(thisobj).closest('.p_wrap').find(".p_all").addClass("checked");
				$(thisobj).closest('.p_wrap').find(".p_col").addClass("checked");
				$(thisobj).closest('.p_wrap').find(".p_row").addClass("checked");
			}
			if (Rc == Rl) {
				$(thisobj).siblings(".p_row").addClass("checked");
			}
			if (Cc == Cl) {
				$(thisobj).closest(".p_wrap").find(".p_col").eq(index - 1).addClass("checked");
			}
			
			isSelect(thisobj,3,0);
		}
	};
	function selectAll(thisobj){//全选
		$(thisobj).toggleClass("checked");
		if ($(thisobj).hasClass("checked")) {
			isSelect(thisobj,0,1);
			$(thisobj).closest(".p_wrap").find(".p_info").addClass("checked");
			$(thisobj).closest(".p_wrap").find(".p_row").addClass("checked");
			$(thisobj).closest(".p_wrap").find(".p_col").addClass("checked");
		} else {
			isSelect(thisobj,0,0);
			$(thisobj).closest(".p_wrap").find(".p_info").removeClass("checked");
			$(thisobj).closest(".p_wrap").find(".p_row").removeClass("checked");
			$(thisobj).closest(".p_wrap").find(".p_col").removeClass("checked");
		}
	};
	function selectRow(thisobj){//选择行
		$(thisobj).toggleClass("checked");
		if ($(thisobj).hasClass("checked")) {
			$(thisobj).siblings("li").addClass("checked");
		} else {
			$(thisobj).siblings("li").removeClass("checked");
		}
		var checkIndex = $(thisobj).closest(".page").find(".p_info.checked").length;
		if (checkIndex >= 1) {
			$(thisobj).closest('.page').find(".choosed_wrap").removeClass("none");
		} else {
			$(thisobj).closest('.page').find(".choosed_wrap").addClass("none");
		}
		if (!$(thisobj).hasClass("checked")) {
			$(thisobj).closest('.page').find(".p_col").removeClass("checked");
			$(thisobj).closest('.page').find(".p_all").removeClass("checked");
		}

		var Al = $(thisobj).closest(".p_wrap").find(".p_info").length;
		if (checkIndex == Al) {
			$(thisobj).closest('.p_wrap').find(".p_all").addClass("checked");
			$(thisobj).closest('.p_wrap').find(".p_col").addClass("checked");
		}
		isSelect(thisobj,1,0);
	};
	function selectCol(thisobj){//选择列
		$(thisobj).toggleClass("checked");
		var index = $(thisobj).index();
		var _this = $(thisobj);
		$(thisobj).closest(".p_wrap").find(".p_lists").each(function() {
			if (_this.hasClass("checked")) {
				$(thisobj).closest(".p_wrap").find("[data-group='" + index + "']").addClass("checked");
			} else {
				$(thisobj).closest(".p_wrap").find("[data-group='" + index + "']").removeClass("checked");
			}
		});
		var checkIndex = $(thisobj).closest(".page").find(".p_info.checked").length;
		if (checkIndex >= 1) {
			$(thisobj).closest('.page').find(".choosed_wrap").removeClass("none");
		} else {
			$(thisobj).closest('.page').find(".choosed_wrap").addClass("none");
		}
		if (!$(thisobj).hasClass("checked")) {
			$(thisobj).closest('.page').find(".p_row").removeClass("checked");
			$(thisobj).closest('.page').find(".p_all").removeClass("checked");
		}

		var Al = $(thisobj).closest(".p_wrap").find(".p_info").length;
		if (checkIndex == Al) {
			$(thisobj).closest('.p_wrap').find(".p_all").addClass("checked");
			$(thisobj).closest('.p_wrap').find(".p_row").addClass("checked");
		}
		isSelect(thisobj,2,0);
	}
	return {
		saveInfo : saveInfo,
		init1:init,
		isSelect:isSelect,
		unique:unique,
		getSubData:getSubData,
		openEmptyDoor:openEmptyDoor,
		openDoor:openDoor,
		setGnum:setGnum,
		chooseGZGoods:chooseGZGoods,
		chooseGoods:chooseGoods,
		colseChoose:colseChoose,
		setNum:setNum,
		beforeSetNum:beforeSetNum,
		getSelectIds:getSelectIds,
		beforeSetPrice:beforeSetPrice,
		clickSingle:clickSingle,
		selectAll:selectAll,
		selectRow:selectRow,
		selectCol:selectCol,
		savePrice:savePrice
	};
});