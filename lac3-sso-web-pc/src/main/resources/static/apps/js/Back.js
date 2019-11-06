
function initTabBack(){
	if(tabInits["Back"]){
		return;
    }
	
	tabInits["Back"] = true;
	
	$("#btn_Back_cancel").click(function() {
		$('.btn-Back').parent().removeClass("active");
		$('#tab_Back').removeClass("active");
	});
	
	$("#btn_Back_ok").click(function() {
		if (!$("#f_Back").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);

		var twfContext = {};
		twfContext.resultDesc = $("#resultDesc4Back").val();
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/back?id="+id+"&uuid="+uuid,
			data : JSON2.stringify(twfContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LacTab.closeThisTab(ret);
					LAC.tip('回退成功!', 'success');
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
	
	$("#f_Back").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
}