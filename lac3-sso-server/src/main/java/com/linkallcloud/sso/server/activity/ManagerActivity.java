package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.sso.activity.IManagerActivity;
import com.linkallcloud.sso.domain.Manager;
import com.linkallcloud.sso.exception.AccountException;
import com.linkallcloud.sso.exception.AuthException;
import com.linkallcloud.sso.server.dao.IManagerDao;

@Component
public class ManagerActivity extends BaseActivity<Manager, IManagerDao> implements IManagerActivity {

	@Autowired
	private IManagerDao managerDao;

	@Override
	public IManagerDao dao() {
		return managerDao;
	}

	@Override
	public Manager fecthByMobile(Trace t, String mobile) {
		return dao().fecthByMobile(t, mobile);
	}

	@Override
	public Manager fecthByLoginName(Trace t, String loginName) {
		return dao().fecthByLoginName(t, loginName);
	}

	@Override
	public Manager loginValidate(Trace t, String loginName, String password) {
		if (Strings.isBlank(loginName)) {
			throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
		}
		if (Strings.isBlank(password)) {
			throw new AuthException("10002001", "登录名或者密码错误，请重新输入！");
		}

		Manager dbAccount = dao().fecthByLoginName(t, loginName);
		if (dbAccount == null) {
			throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
		}
		if (dbAccount.getStatus() != 0) {
			throw new AuthException("10002", "您的账号被限制登陆，请联系管理员！");
		}

		// setClientInfo2Cache(lvo.getLoginName(), lvo.getClient());

		if (Securities.validePassword4Md5Src(password, dbAccount.getSalt(), dbAccount.getPasswd())) {
			return dbAccount;
		}
		throw new AuthException("10002000", "登录名或者密码错误，请重新输入！");
	}

	@Override
	public boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd) {
		Manager account = this.fetchByIdUuid(t, id, uuid);
		if (account != null) {
			if (Securities.validePassword4Md5Src(oldPwd, account.getSalt(), account.getPasswd())) {
				account.setSalt(account.generateUuid());
				account.setPasswd(Securities.password4Md5Src(newPwd, account.getSalt()));
				int rows = dao().update(t, account);
				if (rows > 0) {
					log.debug("update密码 成功，tid：" + t.getTid() + ", id:" + account.getId());
				} else {
					log.error("update密码 失败，tid：" + t.getTid() + ", id:" + account.getId());
				}
				return retBool(rows);
			}
		}
		return false;
	}

	@Override
	protected void before(Trace t, boolean isNew, Manager entity) {
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
