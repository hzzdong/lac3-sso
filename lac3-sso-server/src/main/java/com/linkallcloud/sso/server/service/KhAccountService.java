package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.sso.activity.IKhAccountActivity;
import com.linkallcloud.sso.domain.KhAccount;
import com.linkallcloud.sso.service.IKhAccountService;

@Service
@Transactional(readOnly = true)
public class KhAccountService extends AccountService<KhAccount, IKhAccountActivity> implements IKhAccountService {
	
	@Autowired
	private IKhAccountActivity khAccountActivity;

	@Override
	public IKhAccountActivity activity() {
		return khAccountActivity;
	}

}
