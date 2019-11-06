package com.linkallcloud.sso.manager;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.Account;

public interface IAccountManager extends IManager<Account> {
	
	/*
	 * 支持loginname，mobile，email
	 */
	Account fetchByLoginAccount(Trace t, String loginAccount);
	Account fetchByLoginname(Trace t, String loginname);
    Account fetchByMobile(Trace t, String mobile);
    Account fetchByEmail(Trace t, String email);

    /*
	 * 支持loginname，mobile，email
	 */
    Account loginValidate(Trace t, String account, String password);

    boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);
    boolean updatePassword(Trace t, String loginname, String oldPwd, String newPwd);

    Account fetchByWechatOpenId(Trace t, String openid);
    boolean updateWechatOpenId(Trace t, Long accountId, String openid);
    
    Account fetchByAlipayOpenId(Trace t, String openid);
    boolean updateAlipayOpenId(Trace t, Long accountId, String openid);

}
