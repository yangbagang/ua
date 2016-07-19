var dialog = {
	Html: '<div class="mui-backdrop tipsDialog">' + 
			'<div class="dialog">' + 
		'</div></div>',
	_show: function(iconName, msg) {
		$("body").append(dialog.Html);
	},
	_close: function() {
		$(".cancel, .confirm").on("click", function() {
			$(this).closest(".tipsDialog").remove();
		});
	},
	_alert: function(iconName, msg) {
		dialog._show();
		$(".tipsDialog").find(".dialog").addClass("alert_dialog");
		var tips = '<p class="alertCon"><span class="dialog_icon ' + iconName + '"></span><span class="msg">' + msg + '</span></p>';
		$(".tipsDialog").find(".dialog").html(tips);
		setTimeout(function() {
			$(".tipsDialog").remove();
		}, 2000);
	},
	_delete: function(id, msg) {
		dialog._show();
		$(".tipsDialog").find(".dialog").addClass("delete_dialog");
		var tips = '<p class="deleteCon"><span class="msg">' + msg + '</span></p><p class="btn_block"><a href="javascript:;" class="confirm" id="' + id + '">删除</a><a href="javascript:;" class="cancel">取消</a></p>';
		$(".tipsDialog").find(".dialog").html(tips);
		dialog._close();
	},
	_confirm: function(id, msg) {
		dialog._show();
		var tips = '<p class="tipsCon"><span class="msg">' + msg + '</span></p><p class="btn_wrap"><a href="javascript:;" class="cancel">取消</a><a href="javascript:;" class="confirm" id="' + id + '">确定</a></p>';
		$(".tipsDialog").find(".dialog").html(tips);
		dialog._close();
	},
	_tips: function(id, msg) {
		dialog._show();
		var tips = '<p class="tipsCon"><span class="msg">' + msg + '</span></p><p class="btn_wrap"><a href="javascript:;" class="confirm" id="' + id + '">确定</a></p>';
		$(".tipsDialog").find(".dialog").html(tips);
		dialog._close();
	}
}