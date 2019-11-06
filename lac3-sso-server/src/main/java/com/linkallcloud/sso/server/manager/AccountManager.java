package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.manager.IAccountManager;
import com.linkallcloud.sso.service.IAccountService;

@Service(interfaceClass = IAccountManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "账号")
public class AccountManager extends BaseManager<Account, IAccountService> implements IAccountManager {

	@Autowired
	private IAccountService accountService;

	@Override
	protected IAccountService service() {
		return accountService;
	}

	@Override
	public Account fetchByLoginAccount(Trace t, String loginAccount) {
		return service().fetchByLoginAccount(t, loginAccount);
	}

	@Override
	public Account fetchByLoginname(Trace t, String loginname) {
		return service().fetchByLoginname(t, loginname);
	}

	@Override
	public Account fetchByMobile(Trace t, String mobile) {
		return service().fetchByMobile(t, mobile);
	}

	@Override
	public Account fetchByEmail(Trace t, String email) {
		return service().fetchByEmail(t, email);
	}

	@Override
	public Account loginValidate(Trace t, String account, String password) {
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
	public Account fetchByWechatOpenId(Trace t, String openid) {
		return service().fetchByWechatOpenId(t, openid);
	}

	@Override
	public boolean updateWechatOpenId(Trace t, Long accountId, String openid) {
		return service().updateWechatOpenId(t, accountId, openid);
	}

	@Override
	public Account fetchByAlipayOpenId(Trace t, String openid) {
		return service().fetchByAlipayOpenId(t, openid);
	}

	@Override
	public boolean updateAlipayOpenId(Trace t, Long accountId, String openid) {
		return service().updateAlipayOpenId(t, accountId, openid);
	}

}
