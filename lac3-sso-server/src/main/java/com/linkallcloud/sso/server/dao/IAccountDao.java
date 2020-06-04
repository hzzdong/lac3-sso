package com.linkallcloud.sso.server.dao;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Account;

public interface IAccountDao<T extends Account> extends IDao<T> {

	T fetchByLoginname(@Param("t") Trace t, @Param("loginname") String loginname);

	T fetchByMobile(@Param("t") Trace t, @Param("mobile") String mobile);

	T fetchByEmail(@Param("t") Trace t, @Param("email") String email);

	int updateLastLoginDate(@Param("t") Trace t, @Param("id") Long accountId);

	int updatePassword(@Param("t") Trace t, @Param("entity") T entity);

	T fetchByWechatOpenId(@Param("t") Trace t, @Param("openid") String openid);

	int updateWechatOpenId(@Param("t") Trace t, @Param("id") Long accountId, @Param("openid") String openid);

	T fetchByAlipayOpenId(@Param("t") Trace t, @Param("openid") String openid);

	int updateAlipayOpenId(@Param("t") Trace t, @Param("id") Long accountId, @Param("openid") String openid);

}
