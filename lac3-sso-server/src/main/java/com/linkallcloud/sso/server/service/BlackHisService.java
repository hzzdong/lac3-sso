package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.IBlackHisActivity;
import com.linkallcloud.sso.domain.BlackHis;
import com.linkallcloud.sso.service.IBlackHisService;

@Service
@Transactional(rollbackFor = Exception.class)
public class BlackHisService extends BaseService<BlackHis, IBlackHisActivity> implements IBlackHisService {

	@Autowired
	private IBlackHisActivity blackHisActivity;

	@Override
	public IBlackHisActivity activity() {
		return blackHisActivity;
	}

}
