$(function() {

	$('input').iCheck({
		checkboxClass : 'icheckbox_square-blue',
		radioClass : 'iradio_square-blue',
		increaseArea : '20%' // optional
	});
	
	function refreshImageCode(){
		var $cc = $("#vcodeImage")
		var src = $cc.attr("src");
		var index = src.indexOf("?");
		$cc.attr("src",(index>=0)?(src.substring(0,index) + getTimestamp("?")):(src + getTimestamp("?")));
	}

	function getTimestamp(connetor) {
		return connetor+"t="+(new Date()).getTime();
	}
	
	$(".refresh-code").on('click', function() {
		refreshImageCode();
	});
	

	$("body").keydown(function() {
		if (event.keyCode == "13") {// keyCode=13是回车键
			$('#btn_login').click();
		}
	});
	
	function showErrorInfo(message){
		$(".agile-field-error").html(message || "您的账号无法登陆，请联系管理员。");
		$(".agile-field-error").show();
	}

	$('#btn_login').on('click', function() {
		var username = $.trim($("#username").val());
		var password = $.trim($("#password").val());
		var vcode = $.trim($("#vcode").val());
		var rememberMe = $("#rememberMe").is(":checked") ? "true" : "false";

		if (vcode == "") {
			showErrorInfo("验证码不能为空，请重新输入！");
			$("#vcode").focus();
		} else if (username == "") {
			showErrorInfo("用户登录帐号不能为空，请重新输入！");
			$("#username").focus();
		} else if (password == "") {
			showErrorInfo("密码不能为空，请重新输入！");
			$("#password").focus();
		} else if (password.length < 6) {
			showErrorInfo("密码长度不能小于6，请重新输入！");
			$("#password").focus();
		} else {
			var mpass = hex_md5(password);
			//var weakPwdPatten = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/;
			var weakPwdPatten = /^.*(?=.{8,16})(?=.*\d)(?=.*[a-zA-Z]{2,})(?=.*[!@#$%^&*?\(\)]).*$/;
			var pwdStrength = weakPwdPatten.test(password);
			var lvo = {
				from : $("#from").val(),
				service : $("#service").val(),
				username : username,
				password : mpass,
				lt : $("#lt").val(),
				warn : $("#warn").val(),
				pwdStrength: pwdStrength,
				vcode : vcode,
				rememberMe : rememberMe
			};
			$.ajax({
				url : ctx + "/login",
				type : 'post',
				cache : false,
				data : lvo,
				async : false, // 默认为true 异步
				success : function(ret) {
					console.log(JSON2.stringify(ret));
					if(ret && ret.code=="0"){
		        		window.location.href = ret.go;
					} else {
						$("#lt").val(ret.lt);
						refreshImageCode();
						showErrorInfo(ret.message || "您的账号无法登陆，请联系管理员。");
		        	}
				}
			});
		}
	});

});
