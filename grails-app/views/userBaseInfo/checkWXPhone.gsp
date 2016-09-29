<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>填写验证码</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <link rel="stylesheet" href="../../css/plugins/sm-0.6.2.min.css">
    <link rel="stylesheet" href="../../css/plugins/sm-extend-0.6.2.min.css">
    <link rel="stylesheet" href="../../css/index.min.css?t=d147507e">
    <style>
    .qsrzfmm_bt a { display: block; width: 10%; padding: 10px 0; text-align: center; }
    .qsrzfmm_bt span { padding: 15px 5px; }
    .blank_yh img { height: 40px; }
    .mm_box { width: 89%; margin: 10px auto; height: 40px; overflow: hidden; border: 1px solid #ccc; background-color: #fff; }
    .mm_box li { border-right: 1px solid #ccc; height: 40px; line-height: 40px; text-align: center; float: left; width: 16.66%; background: #FFF; }
    .mm_box li:last-child { border-right: none; }
    .xiaq_tb { padding: 5px 0; text-align: center; border-top: 1px solid #ccc; }
    .numb_box { position: absolute; z-index: 10; background: #f5f5f5; width: 100%; bottom: 0px; left: 0px; }
    .numb_box img { margin-bottom: 0; }
    .nub_ggg { border: 1px solid #dadada; overflow: hidden; border-bottom: none; }
    .nub_ggg li { width: 33.3333%; border-bottom: 1px solid #ccc; float: left; text-align: center; font-size: 22px; }
    .nub_ggg li a { display: block; color: #000; height: 50px; line-height: 50px; overflow: hidden; }
    .nub_ggg li a.zj_x { border-left: 1px solid #ccc; border-right: 1px solid #ccc; }
    .nub_ggg li span { display: block; color: #e0e0e0; background: #e0e0e0; height: 50px; line-height: 50px; overflow: hidden; }
    .nub_ggg li span.del img { width: 30%; }
    .zfcg_box img{ width:10%;}
    .spxx_shop td{ color:#7b7b7b; font-size:14px; padding:10px 0;}
    .wzxfcgde_tb img{ width:20.6%;}
    </style>
</head>
<body>
<div class="page">
    <div class="content bindMobile_wrap">
        <h4 class="tc">我们已将验证码发送到<span></span></h4>
        <div class="pwdInput">
            <form>
                <ul class="mm_box">
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                </ul>
                <!-- 正确图标 -->
                <span class="icon icon_confirm none"></span>
                <!-- 错误图标 -->
                <span class="icon icon_error none"></span>
                <input type="hidden" name="phone" id="phone" value="">
                <input type="hidden" name="openid" id="openid" value="">
                <input type="hidden" name="code" id="code" value="">
            </form>
        </div>
        <div class="numb_box">
            <div class="xiaq_tb"> <img src="../../img/jftc_14.jpg" height="10"> </div>
            <ul class="nub_ggg">
                <li><a href="javascript:void(0);">1</a></li>
                <li><a href="javascript:void(0);" class="zj_x">2</a></li>
                <li><a href="javascript:void(0);">3</a></li>
                <li><a href="javascript:void(0);">4</a></li>
                <li><a href="javascript:void(0);" class="zj_x">5</a></li>
                <li><a href="javascript:void(0);">6</a></li>
                <li><a href="javascript:void(0);">7</a></li>
                <li><a href="javascript:void(0);" class="zj_x">8</a></li>
                <li><a href="javascript:void(0);">9</a></li>
                <li><span></span></li>
                <li><a href="javascript:void(0);" class="zj_x">0</a></li>
                <li><span  class="del" > <img src="../../img/jftc_18.jpg"></span></li>
            </ul>
        </div>
        <div class="hbbj"></div>

        <p class="error_msg none">您输入的验证码有误，请重新获取</p>
        <div class="form_group p10">
            <input type="button" id="getCode" class="btn button-fill button-default" value="60秒重新获取">
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="../../js/plugins/zepto-1.1.6.min.js"></script>
<script type="text/javascript" src="../../js/plugins/sm-0.6.2.min.js"></script>
<script type="text/javascript" src="../../js/plugins/sm-extend-0.6.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/viewJs/checkcode.js"></script>

<script>
    $(function () {
        var phone = getQueryString("phone");
        var openid = getQueryString("openid");
        $('#phone').val(phone);
        $('#openid').val(openid);
        $('.tc>span').text(phone);

        $.ajax({
            url:'../../register/recheckphone',
            data:{phone:phone},
            type:'post',
            dataType:'json',
            success:function(data){
                if(data.success){
                    checkCode();
                }
            }
        });

        function checkCode() {

            $.get("../../auth/getRegisterCode",{phone:phone,businessType:100},function (data) {
                if(data.success){
                    $.alert('验证码已发送');
                    showTimeCode(60,"getCode");
                }else{
                    $.alert(data.msg);
                    showTimeCode(60,"getCode");
                }
            });
        }
        function showTimeCode(time,btn){
            if(time>0) {
                $('#'+btn).attr('disabled','disabled');
                $('#'+btn).removeClass("button-success").addClass("button-default");
                $("#"+btn).val(time+"秒重新获取");
                window.setTimeout(function(){
                    showTimeCode(time-1,btn);
                },1000);
            } else {
                $("#"+btn).removeAttr("disabled");
                $('#'+btn).removeClass("button-default").addClass("red-btn");
                $("#"+btn).val("获取验证码");
                $("#"+btn).click(function () {
                    window.location.href = window.location.href;
                    checkCode();
                });
            }
        }
    });
</script>
</html>