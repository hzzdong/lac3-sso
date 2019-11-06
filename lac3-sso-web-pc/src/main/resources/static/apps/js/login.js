$(function() {

	$('input').iCheck({
		checkboxClass : 'icheckbox_square-blue',
		radioClass : 'iradio_square-blue',
		increaseArea : '20%' // optional
	});
	

	$("body").keydown(function() {
		if (event.keyCode == "13") {// keyCode=13是回车键
			$('#btn_login').click();
		}
	});

	$('#btn_login').on('click', function() {
		var username = $.trim($("#username").val());
		var password = $.trim($("#password").val());
		// var vcode = $.trim($("#vcode").val());
		var rememberMe = $("#rememberMe").is(":checked") ? 1 : 0;

		if (username == "") {
			alert("用户登录帐号不能为空，请重新输入！");
		} else if (password == "") {
			alert("密码不能为空，请重新输入！");
		} else if (password.length < 6) {
			alert("密码长度不能小于6，请重新输入！");
		} else {
			var mpass = hex_md5(password);
			//var weakPwdPatten = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/;
			var weakPwdPatten = /^.*(?=.{8,16})(?=.*\d)(?=.*[a-zA-Z]{2,})(?=.*[!@#$%^&*?\(\)]).*$/;
			var pwdStrength = weakPwdPatten.test(password);
			$.ajax({
				url : ctx + "/login",
				type : 'post',
				cache : false,
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON2.stringify({
					loginName : username,
					password : mpass,
					pwdStrength: pwdStrength,
					vcode : base_path,
					rememberMe : rememberMe
				}),
				async : false, // 默认为true 异步
				success : function(ret) {
					//alert(JSON2.stringify(ret));
					if(ret && ret.code=="0"){
						console.log(base_path + ret.data);
		        		window.location.href = ctx + ret.data;
					} else {
		        		alert(ret.message || "您的账号无法登陆，请联系管理员。");
		        	}
				}
			});
		}
	});

});
