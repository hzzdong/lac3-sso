package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.sso.activity.IAccountActivity;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.exception.AccountException;
import com.linkallcloud.sso.exception.ArgException;
import com.linkallcloud.sso.exception.AuthException;
import com.linkallcloud.sso.server.dao.IAccountDao;

@Component
public class AccountActivity extends BaseActivity<Account, IAccountDao> implements IAccountActivity {

	@Autowired
	private IAccountDao accountDao;

	@Override
	public IAccountDao dao() {
		return accountDao;
	}

	@Override
	public Account fetchByLoginname(Trace t, String loginname) {
		return dao().fetchByLoginname(t, loginname);
	}

	@Override
	public Account fetchByMobile(Trace t, String mobile) {
		return dao().fetchByMobile(t, mobile);
	}

	@Override
	public Account fetchByEmail(Trace t, String email) {
		return dao().fetchByEmail(t, email);
	}

	@Override
	public Account loginValidate(Trace t, Account account, String password) {
		if (account == null) {
			throw new AuthException(ArgException.ARG_CODE_ARG, "登录名或者密码错误，请重新输入！");
		}
		if (account.getStatus() != 0) {
			throw new AuthException(ArgException.ARG_CODE_ARG, "您的账号被限制登陆，请联系管理员！");
		}

		// setClientInfo2Cache(lvo.getLoginName(), lvo.getClient());

		if (Securities.validePassword4Md5Src(password, account.getSalt(), account.getPasswd())) {
			dao().updateLastLoginDate(t, account.getId());
			return account;
		}
		throw new AuthException(AuthException.ARG_CODE_AUTH, "登录名或者密码错误，请重新输入！");
	}

	@Override
	public boolean updatePassword(Trace t, Account account, String oldPwd, String newPwd) {
		if (Securities.validePassword4Md5Src(oldPwd, account.getSalt(), account.getPasswd())) {
			account.retSetSalt();
			account.setPasswd(Securities.password4Md5Src(newPwd, account.getSalt()));
			account.dealOldPasswords(newPwd);
			int rows = dao().updatePassword(t, account);
			return retBool(rows);
		}
		throw new AuthException(AuthException.ARG_CODE_AUTH, "旧密码错误，请重新输入！");
	}

	@Override
	public Account fetchByWechatOpenId(Trace t, String openid) {
		return dao().fetchByWechatOpenId(t, openid);
	}

	@Override
	public boolean updateWechatOpenId(Trace t, Long accountId, String openid) {
		int rows = dao().updateWechatOpenId(t, accountId, openid);
		return retBool(rows);
	}

	@Override
	public Account fetchByAlipayOpenId(Trace t, String openid) {
		return dao().fetchByAlipayOpenId(t, openid);
	}

	@Override
	public boolean updateAlipayOpenId(Trace t, Long accountId, String openid) {
		int rows = dao().updateAlipayOpenId(t, accountId, openid);
		return retBool(rows);
	}

	@Override
	protected void before(Trace t, boolean isNew, Account entity) {
		super.before(t, isNew, entity);
		if (exist(t, entity, "loginname", entity.getLoginname())) {
			throw new AccountException(AccountException.ARG_CODE_ACCOUNT, "已经存在相同账号的用户！");
		}

		if (!Strings.isBlank(entity.getMobile())) {
			if (exist(t, entity, "mobile", entity.getMobile())) {
				throw new AccountException(AccountException.ARG_CODE_ACCOUNT, "已经存在相同手机号码的用户！");
			}
		}

		if (!Strings.isBlank(entity.getEmail())) {
			if (exist(t, entity, "email", entity.getEmail())) {
				throw new AccountException(AccountException.ARG_CODE_ACCOUNT, "已经存在相同Email的用户！");
			}
		}

		if (isNew) {// 新增
			entity.setSalt(entity.generateUuid());
			if (Strings.isBlank(entity.getPasswd())) {
				throw new AccountException(AccountException.ARG_CODE_ACCOUNT, "密码不能为空！");
			}
			entity.setPasswd(Securities.password4Md5Src(entity.getPasswd(), entity.getSalt()));
		} else {
			if (!Strings.isBlank(entity.getPasswd())) {
				entity.setSalt(entity.generateUuid());
				entity.setPasswd(Securities.password4Md5Src(entity.getPasswd(), entity.getSalt()));
			}
		}
	}

}
