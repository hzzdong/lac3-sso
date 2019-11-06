package com.linkallcloud.sso.server.dao;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.BlackList;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IBlackListDao extends IDao<BlackList> {

    BlackList fetchByMobile(@Param("t") Trace t, @Param("mobile") String mobile);

    int batchInsertBlackList(@Param("t") Trace t, @Param("entities") List<BlackList> blacks);
}
