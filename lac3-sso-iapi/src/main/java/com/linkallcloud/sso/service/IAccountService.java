package com.linkallcloud.sso.service;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.Account;

public interface IAccountService<T extends Account> extends IService<T> {
	
	/*
	 * 支持loginname，mobile，email
	 */
	T fetchByLoginAccount(Trace t, String loginAccount);
	T fetchByLoginname(Trace t, String loginname);
    T fetchByMobile(Trace t, String mobile);
    T fetchByEmail(Trace t, String email);

    /*
	 * 支持loginname，mobile，email
	 */
    T loginValidate(Trace t, String account, String password);

    boolean updatePassword(Trace t, Long id, String uuid, String oldPwd, String newPwd);
    boolean updatePassword(Trace t, String loginname, String oldPwd, String newPwd);

    T fetchByWechatOpenId(Trace t, String openid);
    boolean updateWechatOpenId(Trace t, Long accountId, String openid);
    
    T fetchByAlipayOpenId(Trace t, String openid);
    boolean updateAlipayOpenId(Trace t, Long accountId, String openid);

}
