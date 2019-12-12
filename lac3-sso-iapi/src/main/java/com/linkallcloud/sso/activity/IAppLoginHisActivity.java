package com.linkallcloud.sso.activity;

import java.util.List;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.AppLoginHis;

public interface IAppLoginHisActivity extends IActivity<AppLoginHis> {
	
	List<AppLoginHis> findEntitiesByTgt(Trace t, String tgt);
	
}
