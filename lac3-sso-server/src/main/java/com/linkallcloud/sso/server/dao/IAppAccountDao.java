package com.linkallcloud.sso.server.dao;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.AppAccount;

public interface IAppAccountDao extends IDao<AppAccount> {

	AppAccount fetch(@Param("t") Trace t, @Param("appId") Long appId, @Param("ssoLoginName") String ssoLoginName);
	AppAccount fetchByAppCode(@Param("t") Trace t, @Param("appCode") String appCode, @Param("ssoLoginName") String ssoLoginName);

}
