package com.linkallcloud.sso.server.dao;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Manager;

public interface IManagerDao extends IDao<Manager> {

	Manager fecthByMobile(@Param("t") Trace t, @Param("mobile") String mobile);

	Manager fecthByLoginName(@Param("t") Trace t, @Param("loginName") String loginName);

}
