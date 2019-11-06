
function initTabReport(){
	if(tabInits["Report"]){
		return;
    }
	
	tabInits["Report"] = true;
	
	
	$("#btn_Report_cancel").click(function() {
		$('.btn-Report').parent().removeClass("active");
		$('#tab_Report').removeClass("active");
	});
	
	$("#btn_Report_ok").click(function() {
		if (!$("#f_Report").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);

		var twfContext = {};
		twfContext.reportDesc = $("#reportDesc4Report").val();
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/report?id="+id+"&uuid="+uuid,
			data : JSON2.stringify(twfContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LacTab.closeThisTab(ret);
					LAC.tip('上报信息提交成功!', 'success');
				} else{
					LAC.tip(ret.message || "系统出错啦！！！", "error");
				}
			},
			error : function(result, b) {
				$(me).attr("disabled", false);
				LAC.tip("系统出错啦！！！", "error");
			}
		});
	});
	
	$("#f_Report").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
}