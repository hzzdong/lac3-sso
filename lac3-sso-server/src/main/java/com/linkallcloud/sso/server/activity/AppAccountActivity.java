package com.linkallcloud.sso.server.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.activity.IAppAccountActivity;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.domain.AppAccount;
import com.linkallcloud.sso.exception.ArgException;
import com.linkallcloud.sso.server.dao.IAccountDao;
import com.linkallcloud.sso.server.dao.IAppAccountDao;
import com.linkallcloud.sso.server.dao.IKhAccountDao;
import com.linkallcloud.sso.server.dao.IYwAccountDao;
import com.linkallcloud.sso.server.kiss.um.ApplicationKiss;
import com.linkallcloud.um.domain.sys.Application;

@Component
public class AppAccountActivity extends BaseActivity<AppAccount, IAppAccountDao> implements IAppAccountActivity {

	@Autowired
	private IAppAccountDao appAccountDao;

	@Autowired
	private IYwAccountDao ywAccountDao;

	@Autowired
	private IKhAccountDao khAccountDao;

	@Autowired
	private ApplicationKiss applicationKiss;

	@Override
	public IAppAccountDao dao() {
		return appAccountDao;
	}

	@Override
	public AppAccount fetch(Trace t, Long appId, String ssoLoginName) {
		return dao().fetch(t, appId, ssoLoginName);
	}

	@Override
	public AppAccount fetchByAppCode(Trace t, String appCode, String ssoLoginName) {
		return dao().fetchByAppCode(t, appCode, ssoLoginName);
	}

	private IAccountDao<? extends Account> getAccountDao(int clazz) {
		if (clazz == 0) {
			return ywAccountDao;
		} else {
			return khAccountDao;
		}
	}

	@Override
	public AppAccount bind(Trace t, Long appId, String ssoLoginName, String appLoginName) {
		if (appId != null && !Strings.isBlank(ssoLoginName) && !Strings.isBlank(appLoginName)) {
			AppAccount dbEntity = fetch(t, appId, ssoLoginName);
			if (dbEntity == null) {
				Application app = applicationKiss.fetchById(t, appId);
				Account account = getAccountDao(app.getClazz()).fetchByLoginname(t, ssoLoginName);
				if (app != null && account != null) {
					AppAccount entity = new AppAccount(app, account, appLoginName);
					int rows = dao().insert(t, entity);
					if (rows == 1) {
						return entity;
					}
				}
			} else {
				throw new ArgException("您的账号已绑定，无需再次绑定。");
			}
		}
		throw new ArgException("账号绑定失败。");
	}

}
