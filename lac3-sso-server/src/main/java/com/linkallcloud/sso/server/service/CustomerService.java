package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.ICustomerActivity;
import com.linkallcloud.sso.domain.Customer;
import com.linkallcloud.sso.service.ICustomerService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerService extends BaseService<Customer, ICustomerActivity> implements ICustomerService {

	@Autowired
	private ICustomerActivity customerActivity;

	@Override
	public ICustomerActivity activity() {
		return customerActivity;
	}

	@Override
	public Customer fetchByAccount(Trace t, String account) {
		return activity().fetchByAccount(t, account);
	}

	@Override
	public Customer fetchByCode(Trace t, String code) {
		return activity().fetchByCode(t, code);
	}

	@Override
	public Customer loginValidate(Trace t, String loginName, String password) {
		return activity().loginValidate(t, loginName, password);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd) {
		return activity().updatePassword(t, userId, userUuid, oldPwd, newPwd);
	}
}
