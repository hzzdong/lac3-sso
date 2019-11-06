package com.linkallcloud.sso.server.dao;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.TwfDefine;

public interface ITwfDefineDao extends IDao<TwfDefine> {

	TwfDefine fetchByCode(@Param("t") Trace t, @Param("code") String code);

}
