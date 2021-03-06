
function initTabAnswer(){
	if(tabInits["Answer"]){
		return;
    }
	
	tabInits["Answer"] = true;
	
	$("#btn_Answer_cancel").click(function() {
		$('.btn-Answer').parent().removeClass("active");
		$('#tab_Answer').removeClass("active");
	});
	
	$("#btn_Answer_ok").click(function() {
		if (!$("#f_Answer").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);

		var twfContext = {};
		twfContext.resultDesc = $("#resultDesc4Answer").val();
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/answer?id="+id+"&uuid="+uuid,
			data : JSON2.stringify(twfContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LacTab.closeThisTab(ret);
					LAC.tip('答复成功!', 'success');
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
	
	$("#f_Answer").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
}