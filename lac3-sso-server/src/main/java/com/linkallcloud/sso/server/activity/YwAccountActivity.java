package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.activity.IYwAccountActivity;
import com.linkallcloud.sso.domain.YwAccount;
import com.linkallcloud.sso.server.dao.IYwAccountDao;

@Component
public class YwAccountActivity extends AccountActivity<YwAccount, IYwAccountDao> implements IYwAccountActivity {

	@Autowired
	private IYwAccountDao ywAccountDao;

	@Override
	public IYwAccountDao dao() {
		return ywAccountDao;
	}

}
