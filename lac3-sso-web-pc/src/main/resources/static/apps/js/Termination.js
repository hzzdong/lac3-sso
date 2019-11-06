
function initTabTermination(){
	if(tabInits["Termination"]){
		return;
    }
	
	tabInits["Termination"] = true;
	
	$("#btn_Termination_cancel").click(function() {
		$('.btn-Termination').parent().removeClass("active");
		$('#tab_Termination').removeClass("active");
	});
	
	$("#btn_Termination_ok").click(function() {
		if (!$("#f_Termination").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);

		var termiContext = {};
		termiContext.resultDesc = $("#resultDesc4Termination").val();
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/terminate?id="+instId+"&uuid="+instUuid,
			data : JSON2.stringify(termiContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LacTab.closeThisTab(ret);
					LAC.tip('流程终止成功!', 'success');
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
	
	$("#f_Termination").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
}