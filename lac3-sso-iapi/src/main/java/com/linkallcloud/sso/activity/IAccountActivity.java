package com.linkallcloud.sso.activity;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Account;

public interface IAccountActivity<T extends Account> extends IActivity<T> {

	T fetchByLoginname(Trace t, String loginname);
	T fetchByMobile(Trace t, String mobile);
	T fetchByEmail(Trace t, String email);

	T loginValidate(Trace t, T account, String password);

	boolean updatePassword(Trace t, T account, String oldPwd, String newPwd);

	T fetchByWechatOpenId(Trace t, String openid);
	boolean updateWechatOpenId(Trace t, Long accountId, String openid);

	T fetchByAlipayOpenId(Trace t, String openid);
	boolean updateAlipayOpenId(Trace t, Long accountId, String openid);

}
