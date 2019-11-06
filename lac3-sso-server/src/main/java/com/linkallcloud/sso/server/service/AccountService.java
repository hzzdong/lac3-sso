package com.linkallcloud.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.IAccountActivity;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.exception.ArgException;
import com.linkallcloud.sso.exception.AuthException;
import com.linkallcloud.sso.service.IAccountService;

@Service
@Transactional(rollbackFor = Exception.class)
public class AccountService extends BaseService<Account, IAccountActivity> implements IAccountService {

	@Autowired
	private IAccountActivity accountActivity;

	@Override
	public IAccountActivity activity() {
		return accountActivity;
	}

	@Override
	public Account fetchByLoginAccount(Trace t, String loginAccount) {
		if (Strings.isBlank(loginAccount)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "loginAccount不能为空");
		}
		Account dbAccount = activity().fetchByLoginname(t, loginAccount);
		if (dbAccount == null) {
			dbAccount = activity().fetchByMobile(t, loginAccount);
		}
		if (dbAccount == null) {
			dbAccount = activity().fetchByEmail(t, loginAccount);
		}
		return dbAccount;
	}

	@Override
	public Account fetchByLoginname(Trace t, String loginname) {
		if (Strings.isBlank(loginname)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "loginname不能为空");
		}
		return activity().fetchByLoginname(t, loginname);
	}

	@Override
	public Account fetchByMobile(Trace t, String mobile) {
		if (Strings.isBlank(mobile)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "mobile不能为空");
		}
		return activity().fetchByMobile(t, mobile);
	}

	@Override
	public Account fetchByEmail(Trace t, String email) {
		if (Strings.isBlank(email)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "email不能为空");
		}
		return activity().fetchByEmail(t, email);
	}

	@Override
	public Account loginValidate(Trace t, String account, String password) {
		if (Strings.isBlank(account)) {
			throw new AuthException(ArgException.ARG_CODE_ARG, "登录名或者密码错误，请重新输入！");
		}
		if (Strings.isBlank(password)) {
			throw new AuthException(ArgException.ARG_CODE_ARG, "登录名或者密码错误，请重新输入！");
		}

		Account dbAccount = fetchByLoginAccount(t, account);
		if (dbAccount == null) {
			throw new AuthException(ArgException.ARG_CODE_ARG, "登录名或者密码错误，请重新输入！");
		}
		if (dbAccount.getStatus() != 0) {
			throw new AuthException(ArgException.ARG_CODE_ARG, "您的账号被限制登陆，请联系管理员！");
		}

		return activity().loginValidate(t, dbAccount, password);
	}

	@Override
	public boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd) {
		if (Strings.isBlank(oldPwd) || Strings.isBlank(newPwd)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "oldPwd和newPwd都不能为空");
		}

		Account da = activity().fetchByIdUuid(t, id, uuid);
		if (da == null) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "id,uuid参数错误，对应的账号不存在");
		}
		return activity().updatePassword(t, da, oldPwd, newPwd);
	}

	@Override
	public boolean updatePassword(Trace t, String loginname, String oldPwd, String newPwd) {
		if (Strings.isBlank(oldPwd) || Strings.isBlank(newPwd)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "oldPwd和newPwd都不能为空");
		}
		Account da = activity().fetchByLoginname(t, loginname);
		if (da == null) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "loginname参数错误，对应的账号不存在");
		}
		return activity().updatePassword(t, da, oldPwd, newPwd);
	}

	@Override
	public Account fetchByWechatOpenId(Trace t, String openid) {
		if (Strings.isBlank(openid)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "openid不能为空");
		}
		return activity().fetchByWechatOpenId(t, openid);
	}

	@Override
	public boolean updateWechatOpenId(Trace t, Long accountId, String openid) {
		if (accountId == null || Strings.isBlank(openid)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "accountId和openid都不能为空");
		}
		return activity().updateWechatOpenId(t, accountId, openid);
	}

	@Override
	public Account fetchByAlipayOpenId(Trace t, String openid) {
		if (Strings.isBlank(openid)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "openid不能为空");
		}
		return activity().fetchByAlipayOpenId(t, openid);
	}

	@Override
	public boolean updateAlipayOpenId(Trace t, Long accountId, String openid) {
		if (accountId == null || Strings.isBlank(openid)) {
			throw new ArgException(ArgException.ARG_CODE_ARG, "accountId和openid都不能为空");
		}
		return activity().updateAlipayOpenId(t, accountId, openid);
	}

}
