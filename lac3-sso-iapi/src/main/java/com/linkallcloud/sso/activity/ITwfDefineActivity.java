package com.linkallcloud.sso.activity;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.TwfDefine;

public interface ITwfDefineActivity extends IActivity<TwfDefine> {
	
	TwfDefine fetchByCode(Trace t, String code);

}
