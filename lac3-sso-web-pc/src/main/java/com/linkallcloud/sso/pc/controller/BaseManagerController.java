package com.linkallcloud.sso.pc.controller;

import java.util.ArrayList;
import java.util.List;

import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.lang.Strings;

public abstract class BaseManagerController {

	protected static Tree menuRoot = initMenuTree();

	public String[] getMenuPermissionResources() {
		List<String> result = new ArrayList<String>();
		loadMenuPermissionResources(menuRoot, result);
		return result.toArray(new String[result.size()]);
	}

	private void loadMenuPermissionResources(Tree item, List<String> resources) {
		if (!Strings.isBlank(item.getGovCode())) {
			resources.add(item.getGovCode());
		}

		if (item.getChildren() != null && !item.getChildren().isEmpty()) {
			for (Tree child : item.getChildren()) {
				loadMenuPermissionResources(child, resources);
			}
		}
	}

	private static Tree initMenuTree() {
		Tree root = new Tree("-1", null, "统一认证");
		Tree children01 = new Tree("1", root.getId(), "在线管理", "OnLine", "/LoginHis/online");
		root.addChild(children01);

		Tree children02 = new Tree("2", root.getId(), "账号管理");
		root.addChild(children02);
		Tree subChildren0201 = new Tree("21", children02.getId(), "SSO账号", "Account", "/Account/main");
		children02.addChild(subChildren0201);
		Tree subChildren0202 = new Tree("22", children02.getId(), "应用账号", "AppAccount", "/AppAccount/main");
		children02.addChild(subChildren0202);

		Tree children03 = new Tree("3", root.getId(), "锁管理");
		root.addChild(children03);
		Tree subChildren0301 = new Tree("31", children03.getId(), "账号锁", "AccountLock", "/AccountLock/main");
		children03.addChild(subChildren0301);
		Tree subChildren0302 = new Tree("32", children03.getId(), "IP锁", "IpLock", "/IpLock/main");
		children03.addChild(subChildren0302);

		Tree children04 = new Tree("4", root.getId(), "黑名单管理");
		root.addChild(children04);
		Tree subChildren0401 = new Tree("41", children04.getId(), "账号黑名单", "AccountBlack", "/AccountBlack/main");
		children04.addChild(subChildren0401);
		Tree subChildren0402 = new Tree("42", children04.getId(), "IP黑名单", "IpBlack", "/IpBlack/main");
		children04.addChild(subChildren0402);

		Tree children05 = new Tree("5", root.getId(), "日志监控");
		root.addChild(children05);
		Tree subChildren0501 = new Tree("51", children05.getId(), "登录日志", "LoginHis", "/LoginHis/main");
		children05.addChild(subChildren0501);
		Tree subChildren0502 = new Tree("52", children05.getId(), "修改密码日志", "PasswdLog", "/PasswdLog/main");
		children05.addChild(subChildren0502);
		Tree subChildren0503 = new Tree("53", children05.getId(), "锁/解锁日志", "LockHis", "/LockHis/main");
		children05.addChild(subChildren0503);
		Tree subChildren0504 = new Tree("54", children05.getId(), "黑名单日志", "BlackHis", "/BlackHis/main");
		children05.addChild(subChildren0504);
//		Tree subChildren0505 = new Tree("55", children05.getId(), "服务票据", "StLog", "/StLog/main");
//		children05.addChild(subChildren0505);
//		Tree subChildren0506 = new Tree("56", children05.getId(), "授权票据", "TgtLog", "/TgtLog/main");
//		children05.addChild(subChildren0506);
//		Tree subChildren0507 = new Tree("57", children05.getId(), "代理票据", "PtLog", "/PtLog/main");
//		children05.addChild(subChildren0507);

		Tree children06 = new Tree("6", root.getId(), "系统管理");
		root.addChild(children06);
		Tree subChildren0601 = new Tree("61", children06.getId(), "系统设置", "SysSetup", "/SysSetup/main");
		children06.addChild(subChildren0601);
		Tree subChildren0602 = new Tree("62", children06.getId(), "管理员管理", "Manager", "/Manager/main");
		children06.addChild(subChildren0602);
		Tree subChildren0603 = new Tree("63", children06.getId(), "操作日志", "log", "/log/main");
		children06.addChild(subChildren0603);

		Tree children07 = new Tree("7", root.getId(), "帮助", "Help", "/Help/main");
		root.addChild(children07);

		return root;
	}

}
