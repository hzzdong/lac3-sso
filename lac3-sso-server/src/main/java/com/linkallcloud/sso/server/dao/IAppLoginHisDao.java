package com.linkallcloud.sso.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.AppLoginHis;

public interface IAppLoginHisDao extends IDao<AppLoginHis> {

	List<AppLoginHis> findEntitiesByTgt(@Param("t") Trace t, @Param("tgt") String tgt);

}
