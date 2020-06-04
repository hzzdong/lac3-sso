package com.linkallcloud.sso.server.manager;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.manager.IAccountManager;
import com.linkallcloud.sso.service.IAccountService;

public abstract class AccountManager<T extends Account, TS extends IAccountService<T>> extends BaseManager<T, TS>
		implements IAccountManager<T> {

	@Override
	public T fetchByLoginAccount(Trace t, String loginAccount) {
		return service().fetchByLoginAccount(t, loginAccount);
	}

	@Override
	public T fetchByLoginname(Trace t, String loginname) {
		return service().fetchByLoginname(t, loginname);
	}

	@Override
	public T fetchByMobile(Trace t, String mobile) {
		return service().fetchByMobile(t, mobile);
	}

	@Override
	public T fetchByEmail(Trace t, String email) {
		return service().fetchByEmail(t, email);
	}

	@Override
	public T loginValidate(Trace t, String account, String password) {
		return service().loginValidate(t, account, password);
	}

	@Override
	public boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd) {
		return service().updatePassword(t, id, uuid, oldPwd, newPwd);
	}

	@Override
	public boolean updatePassword(Trace t, String loginname, String oldPwd, String newPwd) {
		return service().updatePassword(t, loginname, oldPwd, newPwd);
	}

	@Override
	public T fetchByWechatOpenId(Trace t, String openid) {
		return service().fetchByWechatOpenId(t, openid);
	}

	@Override
	public boolean updateWechatOpenId(Trace t, Long accountId, String openid) {
		return service().updateWechatOpenId(t, accountId, openid);
	}

	@Override
	public T fetchByAlipayOpenId(Trace t, String openid) {
		return service().fetchByAlipayOpenId(t, openid);
	}

	@Override
	public boolean updateAlipayOpenId(Trace t, Long accountId, String openid) {
		return service().updateAlipayOpenId(t, accountId, openid);
	}

}
