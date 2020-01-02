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
	
	$(".refresh-code").on('click', function() {
		refreshImageCode();
	});

	function showErrorInfo(message){
		$(".agile-field-error").html(message || "系统错误，请联系管理员。");
		$(".agile-field-error").show();
	}

	$('#btn_modify_password').on('click', function() {
		var oldPassword = $.trim($("#oldPassword").val());
		var password = $.trim($("#password").val());
		var password2 = $.trim($("#password2").val());
		var vcode = $.trim($("#vcode").val());

		if (vcode == "") {
			showErrorInfo("验证码不能为空，请重新输入！");
			$("#vcode").focus();
			return;
		} else if (oldPassword == "") {
			showErrorInfo("原密码不能为空，请重新输入！");
			$("#username").focus();
		} else if (password == "") {
			showErrorInfo("新密码不能为空，请重新输入！");
			$("#password").focus();
		} else if (password.length < 6) {
			showErrorInfo("新密码长度不能小于6，请重新输入！");
			$("#password").focus();
		} else if (password2 == "") {
			showErrorInfo("确认密码不能为空，请重新输入！");
			$("#password2").focus();
		} else if (password2 != password) {
			showErrorInfo("新密码和确认密码不一致，请重新输入！");
			$("#password2").focus();
		} else {
			//var weakPwdPatten = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/;
			var weakPwdPatten = /^.*(?=.{8,16})(?=.*\d)(?=.*[a-zA-Z]{2,})(?=.*[!@#$%^&*?\(\)]).*$/;
			var pwdStrength = weakPwdPatten.test(password);
			if(!pwdStrength){
				showErrorInfo("新密码强度不够，请根据“密码规则”重新输入！");
				$("#password").focus();
				return;
			}
			
			var mpass = hex_md5(password);
			var moldpass = hex_md5(oldPassword);
			//var params = "oldPassword="+moldpass+"&password="+mpass+"&vcode="+vcode;
			$.ajax({
				url : ctx + "/modifyPassword?oldPassword="+moldpass+"&password="+mpass+"&vcode="+vcode,
				type : 'post',
				cache : false,
				dataType:'json',
        	    contentType: "application/json; charset=utf-8",
				success : function(ret) {
					if(ret && ret.code=="0"){
						alert("密码修改成功！");
		        		window.location.href = ctx + "/generic";
					} else {
						refreshImageCode();
						showErrorInfo(ret.message || "系统错误，请联系管理员。");
		        	}
				}
			});
		}
	});

});
