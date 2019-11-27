package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.IManagerActivity;
import com.linkallcloud.sso.domain.Manager;
import com.linkallcloud.sso.service.IManagerService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ManagerService extends BaseService<Manager, IManagerActivity> implements IManagerService {

	@Autowired
	private IManagerActivity managerActivity;

	@Override
	public IManagerActivity activity() {
		return managerActivity;
	}

	@Override
	public Manager fecthByMobile(Trace t, String mobile) {
		return activity().fecthByMobile(t, mobile);
	}

	@Override
	public Manager fecthByLoginName(Trace t, String loginName) {
		return activity().fecthByLoginName(t, loginName);
	}

	@Override
	public Manager loginValidate(Trace t, String loginName, String password) {
		return activity().loginValidate(t, loginName, password);
	}

	@Override
	public boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd) {
		return activity().updatePassword(t, id, uuid, oldPwd, newPwd);
	}

}
