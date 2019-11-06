package com.linkallcloud.sso.server.dao;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Account;

public interface IAccountDao extends IDao<Account> {

	Account fetchByLoginname(@Param("t") Trace t, @Param("loginname") String loginname);

	Account fetchByMobile(@Param("t") Trace t, @Param("mobile") String mobile);

	Account fetchByEmail(@Param("t") Trace t, @Param("email") String email);

	int updateLastLoginDate(@Param("t") Trace t, @Param("id") Long accountId);

	int updatePassword(@Param("t") Trace t, @Param("entity") Account entity);

	Account fetchByWechatOpenId(@Param("t") Trace t, @Param("openid") String openid);

	int updateWechatOpenId(@Param("t") Trace t, @Param("id") Long accountId, @Param("openid") String openid);

	Account fetchByAlipayOpenId(@Param("t") Trace t, @Param("openid") String openid);

	int updateAlipayOpenId(@Param("t") Trace t, @Param("id") Long accountId, @Param("openid") String openid);

}
