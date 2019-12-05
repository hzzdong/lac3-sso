package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.sso.activity.ILacWebLogActivity;
import com.linkallcloud.sso.domain.LacWebLog;
import com.linkallcloud.sso.server.dao.ILacWebLogDao;

@Component
public class LacWebLogActivity extends BaseActivity<LacWebLog, ILacWebLogDao> implements ILacWebLogActivity {

	@Autowired
	private ILacWebLogDao lacWebLogDao;

	@Override
	public ILacWebLogDao dao() {
		return lacWebLogDao;
	}

}
