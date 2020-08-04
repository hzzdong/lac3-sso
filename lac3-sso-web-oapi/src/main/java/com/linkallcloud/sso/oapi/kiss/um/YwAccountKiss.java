package com.linkallcloud.sso.oapi.kiss.um;

import org.springframework.stereotype.Component;

import com.linkallcloud.um.domain.sys.YwAccount;

@Component
public class YwAccountKiss extends AccountKiss<YwAccount> {

	@Override
	protected String getAccountOapiUrl() {
		return getOapiUrl() + "/face/YwAccount";
	}

}
