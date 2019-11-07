package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.sso.activity.ILockHisActivity;
import com.linkallcloud.sso.domain.LockHis;
import com.linkallcloud.sso.server.dao.ILockHisDao;

@Component
public class LockHisActivity extends BaseActivity<LockHis, ILockHisDao> implements ILockHisActivity {

	@Autowired
	private ILockHisDao lockHisDao;

	@Override
	public ILockHisDao dao() {
		return lockHisDao;
	}

}
