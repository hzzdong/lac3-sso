package com.linkallcloud.sso.domain;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.Client;
import com.linkallcloud.sh.tuils.Dates;

@ShowName(value = "登录日志", logFields = "id,loginname")
public class LoginHis extends Domain {
	private static final long serialVersionUID = 4039376476335603303L;

	private String loginname;// 登录名
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date loginTime;// 登录时间
	private String appCode;// 登录站点
	private String service;// 站点url
	private String ip;// 访问IP
	private boolean mobi;// 是否移动设备
	private String mobileBrand;// 手机品牌
	private String os;// 操作系统
	private String osVersion;// 操作系统版本
	private String browser;// 浏览器
	private String browserVersion;// 浏览器版本

	private String tgt;// MD5(tgt)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date logoutTime;// 登出时间

	public LoginHis() {
		super();
	}

	public LoginHis(String username, String ip, String from, String serviceId, String tgt) {
		super();
		this.loginname = username;
		this.loginTime = new Date();
		this.appCode = from;
		this.service = serviceId;
		this.ip = ip;
		this.tgt = tgt;
	}

	public LoginHis(String username, String ip, String from, String serviceId, String tgt, Client client) {
		this(username, ip, from, serviceId, tgt);
		if (client != null) {
			this.mobi = client.getMobile();
			this.mobileBrand = client.getMobileBrand();
			this.os = client.getOs();
			this.osVersion = client.getOsVersion();
			this.browser = client.getBrowser();
			this.browserVersion = client.getBrowserVersion();
		}
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public String getLoginTimeStr() {
		if (loginTime == null) {
			return "";
		} else {
			return Dates.formatDate(loginTime, "yyyy-MM-dd HH:mm:ss");
		}
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public boolean isTimeout(int tolerance) {
		if (loginTime != null) {
			return Dates.addSecond(loginTime, tolerance).before(new Date());
		}
		return true;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean getMobi() {
		return mobi;
	}

	public void setMobi(boolean mobi) {
		this.mobi = mobi;
	}

	public String getMobileBrand() {
		return mobileBrand;
	}

	public void setMobileBrand(String mobileBrand) {
		this.mobileBrand = mobileBrand;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getTgt() {
		return tgt;
	}

	public void setTgt(String tgt) {
		this.tgt = tgt;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public String getLogoutTimeStr() {
		if (logoutTime == null) {
			return "";
		} else {
			return Dates.formatDate(logoutTime, "yyyy-MM-dd HH:mm:ss");
		}
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

}
