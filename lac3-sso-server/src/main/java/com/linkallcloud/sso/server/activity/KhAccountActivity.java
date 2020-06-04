package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.activity.IKhAccountActivity;
import com.linkallcloud.sso.domain.KhAccount;
import com.linkallcloud.sso.server.dao.IKhAccountDao;

@Component
public class KhAccountActivity extends AccountActivity<KhAccount, IKhAccountDao> implements IKhAccountActivity {

	@Autowired
	private IKhAccountDao khAccountDao;

	@Override
	public IKhAccountDao dao() {
		return khAccountDao;
	}

}
