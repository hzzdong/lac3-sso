package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.sso.activity.IBlackActivity;
import com.linkallcloud.sso.domain.Black;
import com.linkallcloud.sso.server.dao.IBlackDao;

@Component
public class BlackActivity extends BaseActivity<Black, IBlackDao> implements IBlackActivity {

	@Autowired
	private IBlackDao blackDao;

	@Override
	public IBlackDao dao() {
		return blackDao;
	}

}
