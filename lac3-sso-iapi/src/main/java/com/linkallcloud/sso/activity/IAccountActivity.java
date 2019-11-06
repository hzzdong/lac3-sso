package com.linkallcloud.sso.activity;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Account;

public interface IAccountActivity extends IActivity<Account> {

	Account fetchByLoginname(Trace t, String loginname);
	Account fetchByMobile(Trace t, String mobile);
	Account fetchByEmail(Trace t, String email);

	Account loginValidate(Trace t, Account account, String password);

	boolean updatePassword(Trace t, Account account, String oldPwd, String newPwd);

	Account fetchByWechatOpenId(Trace t, String openid);
	boolean updateWechatOpenId(Trace t, Long accountId, String openid);

	Account fetchByAlipayOpenId(Trace t, String openid);
	boolean updateAlipayOpenId(Trace t, Long accountId, String openid);

}
