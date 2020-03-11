package com.linkallcloud.sso.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.principal.AccountMapping;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.sso.activity.IAppAccountActivity;
import com.linkallcloud.sso.domain.AppAccount;
import com.linkallcloud.sso.exception.ArgException;
import com.linkallcloud.sso.server.kiss.um.ApplicationKiss;
import com.linkallcloud.sso.service.IAppAccountService;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.enums.PasswordMode;
import com.linkallcloud.um.enums.SignAlg;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppAccountService extends BaseService<AppAccount, IAppAccountActivity> implements IAppAccountService {

	@Autowired
	private IAppAccountActivity appAccountActivity;

	@Autowired
	private ApplicationKiss applicationKiss;

	@Override
	public IAppAccountActivity activity() {
		return appAccountActivity;
	}

	@Override
	public AppAccount fetch(Trace t, Long appId, String ssoLoginName) {
		return activity().fetch(t, appId, ssoLoginName);
	}

	@Transactional(readOnly = false)
	@Override
	public AppAccount validBind(Trace t, Long appId, String ssoLoginName, String appLoginName, String appPassword) {
		if (validAppAcount(t, appId, appLoginName, appPassword)) {
			return activity().bind(t, appId, ssoLoginName, appLoginName);
		}
		throw new ArgException("应用账号验证绑定失败！");
	}

	private boolean validAppAcount(Trace t, Long appId, String appLoginName, String appPassword) {
		Application app = applicationKiss.fetchById(t, appId);
		if (app != null && AccountMapping.Mapping.getCode().equals(app.getMappingType())
				&& !Strings.isBlank(app.getAuthAddr())) {
			String password = (PasswordMode.MD5.getCode().equals(app.getAuthPassMode())) ? Lang.md5(appPassword)
					: appPassword;
			Long time = new Date().getTime();
			String mimi = appLoginName + password + time + app.getAuthSignKey();
			String sign = null;
			if (SignAlg.SHA1.getCode().equals(app.getAuthSignAlg())) {
				sign = Lang.sha1(mimi);
			} else {
				sign = Lang.md5(mimi);
			}

			Result<Object> result = authAppAccount(t, app.getAuthAddr(), appLoginName, password, time, sign);
			return (result != null && result.getCode().equals("0"));
		}
		return false;
	}

	private Result<Object> authAppAccount(Trace t, String authAddr, String appLoginName, String password, Long time,
			String sign) {
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("loginName", appLoginName));
		parameters.add(new BasicNameValuePair("password", password));
		parameters.add(new BasicNameValuePair("time", time.toString()));
		parameters.add(new BasicNameValuePair("sign", sign));

		String responseJson = null;
		if (authAddr.startsWith("https")) {
			responseJson = HttpClientFactory.me(true).post(authAddr, parameters);
		} else {
			responseJson = HttpClientFactory.me(false).post(authAddr, parameters);
		}
		if (!Strings.isBlank(responseJson)) {
			return JSON.parseObject(responseJson, new TypeReference<Result<Object>>() {
			});
		}
		return null;
	}

}
