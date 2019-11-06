
function initTabQuickSummary(){
	if(tabInits["QuickSummary"]){
		return;
    }
	
	tabInits["QuickSummary"] = true;
	
	$("#btn_QuickSummary_cancel").click(function() {
		$('.btn-QuickSummary').parent().removeClass("active");
		$('#tab_QuickSummary').removeClass("active");
	});
	
	$("#btn_QuickSummary_ok").click(function() {
		if (!$("#f_QuickSummary").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);

		var twfContext = {};
		twfContext.resultDesc = $("#resultDesc4QuickSummary").val();
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/quickSummary?id="+id+"&uuid="+uuid,
			data : JSON2.stringify(twfContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LacTab.closeThisTab(ret);
					LAC.tip('直接答复成功!', 'success');
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
	
	$("#f_QuickSummary").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
}