package com.linkallcloud.sso.portal.kiss.um;

import org.springframework.stereotype.Component;

import com.linkallcloud.um.domain.sys.KhAccount;

@Component
public class KhAccountKiss extends AccountKiss<KhAccount> {

	@Override
	protected String getAccountOapiUrl() {
		return getOapiUrl() + "/face/KhAccount";
	}

}
