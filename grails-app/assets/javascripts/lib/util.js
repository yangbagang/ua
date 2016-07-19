

function Ajax(param){
	loading("show");
	$.ajax({
		url:param.url,
		data:param.data,
		type:"POST",
		dataType:"json",
		cache:false,
		async:param.async?param.async:false,
		success:function(data){
			loading("hide");
			param.success(data);
		},
		error:function(data){
			loading("hide");
		}
	});
}


//ajax加载动画
function loading(type){
	var loading='<div class="ui-loading"><div id="loadingbg">&nbsp;</div><div class="spinner"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div><div class="bounce4"></div><div class="bounce5"></div></div></div>';
	
	if(type=="show"){
		$("body").find(".ui-loading").remove();
		$("body").append(loading);
		var height=document.documentElement.clientHeight;
		if(document.body.scrollHeight>0){
			height+=document.body.scrollHeight;
		}
		$("#loadingbg").height(height);
	}else{
		$("body").find(".ui-loading").remove();
	}
}

/**
 * 乘法得到金额数据（保留精度问题）
 * 调用例子：var total = Number(parseInt(num)).mul(parseFloat(dj));
 * @param arg
 * @returns {String}
 */
Number.prototype.mul = function (arg)   
{
    var m=0,s1=this.toString(),s2=arg.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
     
    var val = Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
    var m = val.toString();
	var num = m.split(".");
	if(num.length>1){
		var l = num[1];
		if(l.length<2){
			//m = m + "0"; // 两位小数
			m = m; // 一位小数
		}
	}
	return m;
}

/**
 * 加法得到金额数据（保留精度问题）
 * 调用例子：var total = Number(0.09999999).add(0.09999999);
 * @param arg
 * @returns {String}
 */
Number.prototype.add = function(arg){   
    var r1,r2,m;   
    try{r1=this.toString().split(".")[1].length}catch(e){r1=0}   
    try{r2=arg.toString().split(".")[1].length}catch(e){r2=0}   
    m=Math.pow(10,Math.max(r1,r2))   
    
    var val = (this*m+arg*m)/m;
    var m = val.toString();
	var num = m.split(".");
	if(num.length>1){
		var l = num[1];
		if(l.length<2){
			//m = m + "0";// 两位小数
			m = m;// 一位小数
		}
	}
	return m;
}

/**
 * 减法得到金额数据（保留精度问题）
 * 调用例子：var total = Number(-0.09999999).sub(0.00000001);
 * @param arg
 * @returns {String}
 */
Number.prototype.sub = function (arg){   
    return this.add(-arg);   
} 

/**
 * 除法得到金额数据（保留精度问题）
 * 调用例子：var total = Number(0.000001).div(0.00000001);
 * @param arg
 * @returns {String}
 */
Number.prototype.div = function (arg){   
    var t1=0,t2=0,r1,r2;   
    try{t1=this.toString().split(".")[1].length}catch(e){}   
    try{t2=arg.toString().split(".")[1].length}catch(e){}   
    with(Math){   
        r1=Number(this.toString().replace(".",""))   
        r2=Number(arg.toString().replace(".",""))   
        return (r1/r2)*pow(10,t2-t1);   
    }   
}


