package com.linkallcloud.sso.domain;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.um.domain.sys.Application;

@ShowName(value = "应用账号", logFields = "id")
public class AppAccount extends Domain {
	private static final long serialVersionUID = -8903239474549370568L;

	private Long appId;// 应用ID
	private String appCode;// 应用编码
	private String appName;// 应用名称
	
	private String appLoginName;// 应用账号
	
	private Long accountId;// 统一账号id
	private String loginname;// 统一账号
	private String name;// 姓名
	
	private String remark;// 描述

	public AppAccount() {
		super();
	}

	public AppAccount(Application app, Account account, String appLoginName) {
		super();
		this.appId = app.getId();
		this.appCode = app.getCode();
		this.appName = app.getName();
		this.accountId = account.getId();
		this.loginname = account.getLoginname();
		this.name = account.getName();
		this.appLoginName = appLoginName;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppLoginName() {
		return appLoginName;
	}

	public void setAppLoginName(String appLoginName) {
		this.appLoginName = appLoginName;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
