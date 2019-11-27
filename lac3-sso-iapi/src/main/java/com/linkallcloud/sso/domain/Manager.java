package com.linkallcloud.sso.domain;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;

import java.util.Date;

@ShowName(value = "管理员", logFields = "id,name")
public class Manager extends Domain {
	private static final long serialVersionUID = 2675649598306195808L;

	private String loginname;// 登录名
	private String passwd;// 密码
	private String salt;// 密码盐
	private String name;// 名称
	private String mobile;// 手机
	private String email;// 邮箱
	@com.alibaba.fastjson.annotation.JSONField(format = "yyyy-MM-dd")
	private Date birthday;// 生日
	private String remark;// 描述

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
