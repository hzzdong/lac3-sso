package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.sso.activity.IYwAccountActivity;
import com.linkallcloud.sso.domain.YwAccount;
import com.linkallcloud.sso.service.IYwAccountService;

@Service
@Transactional(readOnly = true)
public class YwAccountService extends AccountService<YwAccount, IYwAccountActivity> implements IYwAccountService {

	@Autowired
	private IYwAccountActivity ywAccountActivity;

	@Override
	public IYwAccountActivity activity() {
		return ywAccountActivity;
	}

}
