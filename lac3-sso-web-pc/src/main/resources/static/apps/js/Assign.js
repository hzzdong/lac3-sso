
function setSponsor(){
	$("#Sponsor").val(assignContext.sponsorDep?assignContext.sponsorDep.name:"");
}
function setSuperviseSponsor(){
	$("#SuperviseSponsor").val(assignContext.superviseSponsorDep?assignContext.superviseSponsorDep.name:"");
}

function setCoSponsor(){
	var coSponsorDep = "";
	if(assignContext.coSponsorDeps && assignContext.coSponsorDeps.length>0){
		for(var i=0; i<assignContext.coSponsorDeps.length; i++){
			if(i != 0){
				coSponsorDep += ",";
			}
			coSponsorDep += assignContext.coSponsorDeps[i].name;
		}
	}
	$("#CoSponsor").val(coSponsorDep);
}
function setSuperviseCoSponsor(){
	var coSponsorDep = "";
	if(assignContext.superviseCoSponsorDeps && assignContext.superviseCoSponsorDeps.length>0){
		for(var i=0; i<assignContext.superviseCoSponsorDeps.length; i++){
			if(i != 0){
				coSponsorDep += ",";
			}
			coSponsorDep += assignContext.superviseCoSponsorDeps[i].name;
		}
	}
	$("#SuperviseCoSponsor").val(coSponsorDep);
}

function checkSponsor(treeId){
	if(assignContext.sponsorDep){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.transformToArray(treeObj.getNodes());
		if(nodes && nodes.length > 0){
			var sponsorId = assignContext.sponsorDep.id;
			for(var i=0; i<nodes.length; i++){
				if(nodes[i].id == sponsorId){
					treeObj.selectNode(nodes[i]);
				}
			}
		}
	}
}
function checkSuperviseSponsor(treeId){
	if(assignContext.superviseSponsorDep){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.transformToArray(treeObj.getNodes());
		if(nodes && nodes.length > 0){
			var sponsorId = assignContext.superviseSponsorDep.id;
			for(var i=0; i<nodes.length; i++){
				if(nodes[i].id == sponsorId){
					treeObj.selectNode(nodes[i]);
				}
			}
		}
	}
}

function checkCoSponsor(treeId){
	if(assignContext.coSponsorDeps && assignContext.coSponsorDeps.length>0){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.transformToArray(treeObj.getNodes());
		if(nodes && nodes.length > 0){
			var coSponsorDeps = assignContext.coSponsorDeps;
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
function checkSuperviseCoSponsor(treeId){
	if(assignContext.superviseCoSponsorDeps && assignContext.superviseCoSponsorDeps.length>0){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.transformToArray(treeObj.getNodes());
		if(nodes && nodes.length > 0){
			var coSponsorDeps = assignContext.superviseCoSponsorDeps;
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
	
function initTabAssign(){
	if(tabInits["Assign"]){
		return;
    }
	
	tabInits["Assign"] = true;
	
	$("#Sponsor").lac2FieldTree({
		setting: {
			callback: {
				onClick: function(event, treeId, treeNode, clickFlag) {
					if(!assignContext.sponsorDep){
						assignContext.sponsorDep = {};
					}
					assignContext.sponsorDep.id = treeNode.id;
					assignContext.sponsorDep.name = treeNode.name;
					$("#Sponsor").val(treeNode.name);
				}		
			}
		},
		url: ctx + "/YwDepartment/findBrotherDepartments",
		callback: function(treeId, options, treeData){
			setSponsor();
			checkSponsor(treeId);
		},
		searchable: true,
		expandAll : false,
		cacheEnabled: true
	});
	
	$("#CoSponsor").lac2FieldTree({
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
						assignContext.coSponsorDeps = coSponsorDeps;
						setCoSponsor();
					} else {
						//assignContext.coSponsorDeps = [];
						if(assignContext.coSponsorDeps){
							delete assignContext['coSponsorDeps'];
						}
						$("#CoSponsor").val("");
					}
				}		
			}
		},
		url: ctx + "/YwDepartment/findBrotherDepartments",
		callback: function(treeId, options, treeData){
			setCoSponsor();
			checkCoSponsor(treeId);
			$("#btn-select-CoSponsor").height(60);
		},
		searchable: true,
		expandAll : false,
		cacheEnabled: true
	});
	
	$("#btn_Assign_cancel").click(function() {
		$('.btn-Assign').parent().removeClass("active");
		$('#tab_Assign').removeClass("active");
	});
	$("#btn_Assign_ok").click(function() {
		if (!$("#f_Assign").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);
		
		if(assignContext.coSponsorDeps && assignContext.coSponsorDeps.length>0){
			for(var i=0; i<assignContext.coSponsorDeps.length; i++){
				var coSponsorDep = assignContext.coSponsorDeps[i];
				if(coSponsorDep.id == assignContext.sponsorDep.id){
					$(me).attr("disabled", false);
					alert(assignContext.sponsorDep.name + ",被同时设置成了主办和协办，请重新设置。");
					return;
				}
			}
		}
		
		if(assignContext.args){
			delete assignContext['args'];
		}
		assignContext.complexity = $("#complexity").val();
		assignContext.resultDesc = $("#resultDesc4Assign").val();
		
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/assignBl?id="+id+"&uuid="+uuid,
			data : JSON2.stringify(assignContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LAC.tip('指派成功!', 'success');
					LacTab.closeThisTab(ret);
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
	
	$("#f_Assign").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
	
	$("#SuperviseSponsor").lac2FieldTree({
		setting: {
			callback: {
				onClick: function(event, treeId, treeNode, clickFlag) {
					if(!assignContext.superviseSponsorDep){
						assignContext.superviseSponsorDep = {};
					}
					assignContext.superviseSponsorDep.id = treeNode.id;
					assignContext.superviseSponsorDep.name = treeNode.name;
					$("#SuperviseSponsor").val(treeNode.name);
				}		
			}
		},
		url: ctx + "/YwDepartment/findBrotherDepartments",
		callback: function(treeId, options, treeData){
			setSuperviseSponsor();
			checkSuperviseSponsor(treeId);
		},
		searchable: true,
		expandAll : false,
		cacheEnabled: true
	});
	
	$("#SuperviseCoSponsor").lac2FieldTree({
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
						assignContext.superviseCoSponsorDeps = coSponsorDeps;
						setSuperviseCoSponsor();
					} else {
						//assignContext.superviseCoSponsorDeps = [];
						if(assignContext.superviseCoSponsorDeps){
							delete assignContext['superviseCoSponsorDeps'];
						}
						$("#SuperviseCoSponsor").val("");
					}
				}		
			}
		},
		url: ctx + "/YwDepartment/findBrotherDepartments",
		callback: function(treeId, options, treeData){
			setSuperviseCoSponsor();
			checkSuperviseCoSponsor(treeId);
			$("#btn-select-SuperviseCoSponsor").height(60);
		},
		searchable: true,
		expandAll : false,
		cacheEnabled: true
	});
	
	$("#btn_SuperviseAssign_cancel").click(function() {
		$('.btn-Assign').parent().removeClass("active");
		$('#tab_Assign').removeClass("active");
	});
	
	$("#btn_SuperviseAssign_ok").click(function() {
		if (!$("#f_SuperviseAssign").validationEngine('validate')) {
			return;
		}
		
		var me = this;
		$(me).attr("disabled",true);
		
		if(assignContext.args){
			delete assignContext['args'];
		}
		
		LAC.ajax({
			type : "post",
			dataType : 'json',
			cache : false,
			contentType : "application/json",
			url : ctx + "/FeedbackTwfActivity/assignDb?id="+id+"&uuid="+uuid,
			data : JSON2.stringify(assignContext),
			success : function(ret) {
				$(me).attr("disabled", false);
				if(ret && ret.code=="0"){
					LAC.tip('指派成功!', 'success');
					LacTab.closeThisTab(ret);
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
	
	$("#f_SuperviseAssign").validationEngine("attach", { notShowPrompt : true }).bindValidationEngineHelper();
	
}