package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
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

	@Override
	public void logout(Trace t, String md5Tgt) {
		activity().logout(t, md5Tgt);
	}

	@Override
	public void logout(Trace t, Long id) {
		activity().logout(t, id);
	}

	@Override
	public LoginHis fetchByTgt(Trace t, String md5Tgt) {
		return activity().fetchByTgt(t, md5Tgt);
	}

}
