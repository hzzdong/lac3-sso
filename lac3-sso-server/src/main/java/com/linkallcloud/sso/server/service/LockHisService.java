package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.ILockHisActivity;
import com.linkallcloud.sso.domain.LockHis;
import com.linkallcloud.sso.service.ILockHisService;

@Service
@Transactional(rollbackFor = Exception.class)
public class LockHisService extends BaseService<LockHis, ILockHisActivity> implements ILockHisService {

	@Autowired
	private ILockHisActivity lockHisActivity;

	@Override
	public ILockHisActivity activity() {
		return lockHisActivity;
	}

}
