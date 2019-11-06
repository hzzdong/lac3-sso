
function initTabResultsSummary(){
	if(tabInits["ResultsSummary"]){
		return;
    }
	
	tabInits["ResultsSummary"] = true;
	
	$("#btn_ResultsSummary_cancel").click(function() {
		$('.btn-ResultsSummary').parent().removeClass("active");
		$('#tab_ResultsSummary').removeClass("active");
	});
	
	$("#btn_ResultsSummary_ok").click(function() {
		if (!$("#f_ResultsSummary").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);

		var twfContext = {};
		twfContext.resultDesc = $("#resultDesc4ResultsSummary").val();
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/resultsSummary?id="+id+"&uuid="+uuid,
			data : JSON2.stringify(twfContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LacTab.closeThisTab(ret);
					LAC.tip('结果汇总成功!', 'success');
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
	
	$("#f_ResultsSummary").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
}