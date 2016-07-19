var dialog = {
	Html: '<div class="mui-backdrop tipsDialog">' + 
			'<div class="dialog alertInfo">' + 
		'</div></div>',
	_show: function(iconName, msg) {
		$("body").append(dialog.Html);
	},
	_close: function() {
		$(".cancel, .confirm").on("tap", function() {
			$(this).closest(".tipsDialog").remove();
		});
	},
	_alert: function(iconName, msg) {
		dialog._show();
		var tips = '<p><span class="dialog_icon ' + iconName + '"></span><span class="msg">' + msg + '</span></p>';
		$(".tipsDialog").find(".dialog").html(tips);
		setTimeout(function() {
			$(".tipsDialog").remove();
		}, 2000);
	},
	_delete: function(msg) {
		dialog._show();
		var tips = '<p class="deleteMsg"><span class="msg">' + msg + '</span></p><p class="btn_wrap"><a href="javascript:;" class="cancel">取消</a><a href="javascript:;" class="confirm" id="sure1">确定</a></p>';
		$(".tipsDialog").find(".dialog").html(tips);
		dialog._close();
	}
}