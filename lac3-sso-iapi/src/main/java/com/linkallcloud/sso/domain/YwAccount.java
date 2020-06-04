package com.linkallcloud.sso.domain;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName(value = "运维账号", logFields = "id,name")
public class YwAccount extends Account {
	private static final long serialVersionUID = -864695685605082732L;

	public YwAccount() {
		super();
	}

}
