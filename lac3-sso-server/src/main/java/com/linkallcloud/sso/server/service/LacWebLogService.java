package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.service.BaseBusiLogService;
import com.linkallcloud.sso.activity.ILacWebLogActivity;
import com.linkallcloud.sso.domain.LacWebLog;
import com.linkallcloud.sso.service.ILacWebLogService;

@Service
@Transactional(readOnly = true)
public class LacWebLogService extends BaseBusiLogService<LacWebLog, ILacWebLogActivity> implements ILacWebLogService {

	@Autowired
	private ILacWebLogActivity lacWebLogActivity;

	@Override
	public ILacWebLogActivity activity() {
		return lacWebLogActivity;
	}

}
