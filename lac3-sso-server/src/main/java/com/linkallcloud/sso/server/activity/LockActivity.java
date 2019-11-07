package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.sso.activity.ILockActivity;
import com.linkallcloud.sso.domain.Lock;
import com.linkallcloud.sso.server.dao.ILockDao;

@Component
public class LockActivity extends BaseActivity<Lock, ILockDao> implements ILockActivity {

	@Autowired
	private ILockDao lockDao;

	@Override
	public ILockDao dao() {
		return lockDao;
	}

}
