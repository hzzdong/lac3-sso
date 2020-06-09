$(function() {

	function refreshImageCode(){
		var $cc = $("#vcodeImage")
		var src = $cc.attr("src");
		var index = src.indexOf("?");
		$cc.attr("src",(index>=0)?(src.substring(0,index) + getTimestamp("?")):(src + getTimestamp("?")));
	}

	function getTimestamp(connetor) {
		return connetor+"t="+(new Date()).getTime();
	}
	
	function appendParam2URL(url, name, value) {
		url += (url.match(/\?/) ? "&" : "?") + name + "=" + value;
		return url;
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

	$("select#appClazz").change(function(){
		$("#clazz").val($(this).val());
		console.log($(this).val());
	});

	$('#btn_login').on('click', function() {
		var username = $.trim($("#username").val());
		var password = $.trim($("#password").val());
		var vcode = $.trim($("#vcode").val());
		var rememberMe = $("#rememberMe").is(":checked") ? "1" : "0";
		var clazz = -1;
		if($("#appClazz").is(":hidden")){
			clazz = $("#clazz").val();
		} else {
			clazz = $("#appClazz").val();
		}

		if (vcode == "") {
			showErrorInfo("验证码不能为空，请重新输入！");
			$("#vcode").focus();
			return;
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
				clazz : clazz,
				from : $("#from").val(),
				service : $("#service").val(),
				loginName : username,
				password : mpass,
				lt : $("#lt").val(),
				warn : $("#warn").val(),
				pwdStrength: pwdStrength,
				vcode : vcode,
				rememberMe : rememberMe,
				client:browserOsInfo
			};
			$.ajax({
				url : ctx + "/login",
				type : 'post',
				cache : false,
				dataType:'json',
        	    contentType: "application/json; charset=utf-8",
				data : JSON2.stringify(lvo),
				async : false, // 默认为true 异步
				success : function(ret) {
					//console.log(JSON2.stringify(ret));
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
	
	$('#btn_bind').on('click', function() {
		var username = $.trim($("#username").val());
		var password = $.trim($("#password").val());
		var vcode = $.trim($("#vcode").val());

		if (vcode == "") {
			showErrorInfo("验证码不能为空，请重新输入！");
			$("#vcode").focus();
			return;
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
			var bindvo = {
				from : $("#from").val(),
				fromId : $("#fromId").val(),
				service : $("#service").val(),
				loginName : username,
				password : mpass,
				vcode : vcode,
				client:browserOsInfo
			};
			$.ajax({
				url : ctx + "/doBind",
				type : 'post',
				cache : false,
				dataType:'json',
        	    contentType: "application/json; charset=utf-8",
				data : JSON2.stringify(bindvo),
				async : false, // 默认为true 异步
				success : function(ret) {
					if(ret && ret.code=="0"){
		        		window.location.href = ret.go;
					} else {
						refreshImageCode();
						showErrorInfo(ret.message || "应用账号验证失败，请核对后重试！");
		        	}
				}
			});
		}
	});

});
