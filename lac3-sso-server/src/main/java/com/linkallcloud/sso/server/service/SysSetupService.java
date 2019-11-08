package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.ISysSetupActivity;
import com.linkallcloud.sso.domain.SysSetup;
import com.linkallcloud.sso.service.ISysSetupService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysSetupService extends BaseService<SysSetup, ISysSetupActivity> implements ISysSetupService {

	@Autowired
	private ISysSetupActivity sysSetupActivity;

	@Override
	public ISysSetupActivity activity() {
		return sysSetupActivity;
	}

}
