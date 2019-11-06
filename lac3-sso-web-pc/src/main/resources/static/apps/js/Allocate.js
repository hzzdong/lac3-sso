
function setAllocateSponsor(){
	$("#AllocateSponsor").val(allocateContext.sponsorDep?allocateContext.sponsorDep.name:"");
}
function setAllocateCoSponsor(){
	var coSponsorDep = "";
	if(allocateContext.coSponsorDeps && allocateContext.coSponsorDeps.length>0){
		for(var i=0; i<allocateContext.coSponsorDeps.length; i++){
			if(i != 0){
				coSponsorDep += ",";
			}
			coSponsorDep += allocateContext.coSponsorDeps[i].name;
		}
	}
	$("#AllocateCoSponsor").val(coSponsorDep);
}
function checkAllocateSponsor(treeId){
	if(allocateContext.sponsorDep){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.transformToArray(treeObj.getNodes());
		if(nodes && nodes.length > 0){
			var sponsorId = allocateContext.sponsorDep.id;
			for(var i=0; i<nodes.length; i++){
				if(nodes[i].id == sponsorId){
					treeObj.selectNode(nodes[i]);
				}
			}
		}
	}
}
function checkAllocateCoSponsor(treeId){
	if(allocateContext.coSponsorDeps && allocateContext.coSponsorDeps.length>0){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.transformToArray(treeObj.getNodes());
		if(nodes && nodes.length > 0){
			var coSponsorDeps = allocateContext.coSponsorDeps;
			for(var i=0; i<nodes.length; i++){
				for(var j=0; j<coSponsorDeps.length; j++){
					if(nodes[i].id == coSponsorDeps[j].id ){
						treeObj.checkNode(nodes[i], true, true);
					}
				}
			}
		}
	}
}

function initTabAllocate(){
	if(tabInits["Allocate"]){
		return;
    }
	
	tabInits["Allocate"] = true;
	
	$("#AllocateSponsor").lac2FieldTree({
		setting: {
			callback: {
				onClick: function(event, treeId, treeNode, clickFlag) {
					if(!allocateContext.sponsorDep){
						allocateContext.sponsorDep = {};
					}
					allocateContext.sponsorDep.id = treeNode.id;
					allocateContext.sponsorDep.name = treeNode.name;
					$("#AllocateSponsor").val(treeNode.name);
				}		
			}
		},
		url: ctx + "/YwDepartment/findChildrenDepartments",
		callback: function(treeId, options, treeData){
			setAllocateSponsor();
			checkAllocateSponsor(treeId);
		},
		searchable: true,
		expandAll : false,
		cacheEnabled: true
	});
	
	$("#AllocateCoSponsor").lac2FieldTree({
		setting: {
			view: {
				selectedMulti: false
			},
			check: {
				enable: true,
				chkboxType:{ "Y" : "", "N" : "" }
			},
			callback: {
				onCheck: function(event, treeId, treeNode) {
					var zTree = $.fn.zTree.getZTreeObj(treeId),
					checkNodes = zTree.getCheckedNodes(true);
					if(checkNodes && checkNodes.length >0){
						var coSponsorDeps = [];
						for(var i=0; i<checkNodes.length; i++){
							var nd = checkNodes[i];
							coSponsorDeps.push({id:nd.id, name:nd.name});
						}
						allocateContext.coSponsorDeps = coSponsorDeps;
						setAllocateCoSponsor();
					} else {
						//allocateContext.coSponsorDeps = [];
						if(allocateContext.coSponsorDeps){
							delete allocateContext['coSponsorDeps'];
						}
						$("#AllocateCoSponsor").val("");
					}
				}		
			}
		},
		url: ctx + "/YwDepartment/findChildrenDepartments",
		callback: function(treeId, options, treeData){
			setAllocateCoSponsor();
			checkAllocateCoSponsor(treeId);
			$("#btn-select-AllocateCoSponsor").height(60);
		},
		searchable: true,
		expandAll : false,
		cacheEnabled: true
	});
	
	
	$("#btn_Allocate_cancel").click(function() {
		$('.btn-Allocate').parent().removeClass("active");
		$('#tab_Allocate').removeClass("active");
	});
	
	$("#btn_Allocate_ok").click(function() {
		if (!$("#f_Allocate").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);

		if(allocateContext.args){
			delete allocateContext['args'];
		}
		allocateContext.allocateDesc = $("#allocateDesc4Allocate").val();
		
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/allocate?id="+id+"&uuid="+uuid,
			data : JSON2.stringify(allocateContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LacTab.closeThisTab(ret);
					LAC.tip('下派成功!', 'success');
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
	
	$("#f_Allocate").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
}