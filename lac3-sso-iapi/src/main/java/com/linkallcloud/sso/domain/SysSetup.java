package com.linkallcloud.sso.domain;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName(value = "系统设置", logFields = "id,name")
public class SysSetup extends Domain {
	private static final long serialVersionUID = -7033443810133483120L;

	private String code;// 设置项编号
	private String name;// 设置项名称
	private String value;// 设置项值
	private String remark;// 说明

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
