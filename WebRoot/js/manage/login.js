$(function() {
	$("#pwd").keydown(function(e) { // 回车登录
		var curKey = e.which;
		if (curKey == 13) {
			login();
		}
	});
});

function login() {
	var userName = $("#userName").val();
	if (isEmpty(userName)) {
		wxalert("请输入账号");
		$("#userName").focus();
		return;
	}
	/*if(userName == "cdfma"){
		wxalert("系统忙，刷新页面重试或联系管理员！");
		return;
	}*/
	var password = $("#password").val();
	if (isEmpty(password)) {
		wxalert("请输入密码");
		$("#password").focus();
		return;
	}
	var data = {
		userName : userName,
		password : password
	};
	post("/sys/login/ajaxLogin", data, function(msg) {
		if (msg.errcode == 0) {
			window.location.href = "/sys/index";
		} else {
			alertMsg(msg.errmsg, 2);
		}
	});
}