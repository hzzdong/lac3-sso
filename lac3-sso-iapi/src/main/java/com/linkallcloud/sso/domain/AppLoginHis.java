package com.linkallcloud.sso.domain;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.util.Dates;

@ShowName(value = "应用登录日志", logFields = "id")
public class AppLoginHis extends Domain {
	private static final long serialVersionUID = 4265268144845391980L;

	private String appCode;// 登录站点
	private String service;// 站点url
	private String loginname;// 登录名
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date loginTime;// 登录时间
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date logoutTime;// 登出时间
	private String tgt;// MD5(tgt)
	private Boolean proxy;// 是否代理
	private String proxyChain;// 代理链
	private String pgt;// MD5(pgt)
	private String ppgt;// MD5(parent pgt)

	public AppLoginHis() {
		super();
	}

	/**
	 * pt认证后无后续代理
	 * 
	 * @param username
	 * @param from
	 * @param serviceId
	 * @param tgt
	 * @param pgt
	 */
	public AppLoginHis(String username, String from, String serviceId, String tgt, String ppgt) {
		super();
		this.loginname = username;
		this.loginTime = new Date();
		this.appCode = from;
		this.service = serviceId;
		this.tgt = tgt;
		this.proxy = false;
		this.ppgt = ppgt;

	}

	/**
	 * pt认证后有后续代理
	 * 
	 * @param username
	 * @param from
	 * @param serviceId
	 * @param tgt
	 * @param pgt
	 * @param proxyChain
	 * @param ppgt
	 */
	public AppLoginHis(String username, String from, String serviceId, String tgt, String ppgt, String proxyChain,
			String pgt) {
		this(username, from, serviceId, tgt, ppgt);
		this.proxy = true;
		this.proxyChain = proxyChain;
		this.pgt = pgt;
	}

	/**
	 * st认证后无后续代理
	 * 
	 * @param username
	 * @param from
	 * @param serviceId
	 * @param tgt
	 */
	public AppLoginHis(String username, String from, String serviceId, String tgt) {
		super();
		this.loginname = username;
		this.loginTime = new Date();
		this.appCode = from;
		this.service = serviceId;
		this.tgt = tgt;
		this.proxy = false;
	}

	/**
	 * st认证后有后续代理
	 * 
	 * @param username
	 * @param from
	 * @param serviceId
	 * @param tgt
	 * @param proxyChain
	 * @param pgt
	 */
	public AppLoginHis(String username, String from, String serviceId, String tgt, String proxyChain, String pgt) {
		this(username, from, serviceId, tgt);
		this.proxy = true;
		this.proxyChain = proxyChain;
		this.pgt = pgt;
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

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getTgt() {
		return tgt;
	}

	public void setTgt(String tgt) {
		this.tgt = tgt;
	}

	public Boolean getProxy() {
		return proxy;
	}

	public void setProxy(Boolean proxy) {
		this.proxy = proxy;
	}

	public String getProxyChain() {
		return proxyChain;
	}

	public void setProxyChain(String proxyChain) {
		this.proxyChain = proxyChain;
	}

	public String getPgt() {
		return pgt;
	}

	public void setPgt(String pgt) {
		this.pgt = pgt;
	}

	public String getPpgt() {
		return ppgt;
	}

	public void setPpgt(String ppgt) {
		this.ppgt = ppgt;
	}

	public Tree toTreeNode() {
		Tree t = new Tree(this.getId().toString(), null, this.getLoginname(), this.getAppCode(), true);
		t.addAttribute("url", this.getService());
		t.addAttribute("pgt", this.getPgt());
		t.addAttribute("ppgt", this.getPpgt());
		t.addAttribute("loginTime", Dates.formatDateTime(this.getLoginTime()));
		t.addAttribute("proxy", this.getProxy());
		t.addAttribute("proxyChain", this.getProxyChain());
		return t;
	}

}
