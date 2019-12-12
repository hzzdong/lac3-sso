package com.linkallcloud.sso.activity;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.LoginHis;

public interface ILoginHisActivity extends IActivity<LoginHis> {
	
	void logout(Trace t, String md5Tgt);
	void logout(Trace t, Long id);
	LoginHis fetchByTgt(Trace t, String md5Tgt);
	
}
