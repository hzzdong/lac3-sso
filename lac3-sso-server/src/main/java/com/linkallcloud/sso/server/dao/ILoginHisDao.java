package com.linkallcloud.sso.server.dao;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.LoginHis;

public interface ILoginHisDao extends IDao<LoginHis> {

	void logout(@Param("t") Trace t, @Param("id") Long id);
	LoginHis fetchByTgt(@Param("t") Trace t, @Param("tgt") String md5Tgt);

}
