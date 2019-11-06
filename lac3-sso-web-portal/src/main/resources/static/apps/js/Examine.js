
function initTabExamine(){
	if(tabInits["Examine"]){
		return;
    }
	
	tabInits["Examine"] = true;
	
	$("#btn_Examine_cancel").click(function() {
		$('.btn-Examine').parent().removeClass("active");
		$('#tab_Examine').removeClass("active");
	});
	
	$("#btn_Examine_ok").click(function() {
		if (!$("#f_Examine").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);

		var twfContext = {};
		twfContext.resultState = $("#resultState4Examine").val();
		twfContext.resultDesc = $("#resultDesc4Examine").val();
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/examine?id="+id+"&uuid="+uuid,
			data : JSON2.stringify(twfContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LacTab.closeThisTab(ret);
					LAC.tip('审核成功!', 'success');
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
	
	$("#f_Examine").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
}