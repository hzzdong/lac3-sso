package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.ILoginHisActivity;
import com.linkallcloud.sso.domain.LoginHis;
import com.linkallcloud.sso.service.ILoginHisService;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginHisService extends BaseService<LoginHis, ILoginHisActivity> implements ILoginHisService {

	@Autowired
	private ILoginHisActivity loginHisActivity;

	@Override
	public ILoginHisActivity activity() {
		return loginHisActivity;
	}

}
