package com.linkallcloud.sso.activity;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.SysSetup;

public interface ISysSetupActivity extends IActivity<SysSetup> {
	
	SysSetup fetchByCode(Trace t, String code);
	
}
