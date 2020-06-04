package com.linkallcloud.sso.domain;

import com.linkallcloud.core.domain.annotation.ShowName;

@ShowName(value = "客户账号", logFields = "id,name")
public class KhAccount extends Account {
	private static final long serialVersionUID = 6358395618329617921L;

	public KhAccount() {
		super();
	}

}
