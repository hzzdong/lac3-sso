package com.linkallcloud.sso.activity;

import java.util.List;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Black;

public interface IBlackActivity extends IActivity<Black> {

	void autoBlack(Trace t, Black black);
	Black fetchExistBlack(Trace t, Integer type, String blackTarget, Integer status);
	List<Black> findExistBlacks(Trace t, Integer type, String blackTarget, Integer status);

}
