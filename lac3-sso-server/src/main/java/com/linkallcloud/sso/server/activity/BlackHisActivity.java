package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.sso.activity.IBlackHisActivity;
import com.linkallcloud.sso.domain.BlackHis;
import com.linkallcloud.sso.server.dao.IBlackHisDao;

@Component
public class BlackHisActivity extends BaseActivity<BlackHis, IBlackHisDao> implements IBlackHisActivity {

	@Autowired
	private IBlackHisDao blackHisDao;

	@Override
	public IBlackHisDao dao() {
		return blackHisDao;
	}

}
