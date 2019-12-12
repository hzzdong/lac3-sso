package com.linkallcloud.sso.service;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.AppLoginHis;

public interface IAppLoginHisService extends IService<AppLoginHis> {
	
	List<AppLoginHis> findEntitiesByTgt(Trace t, String tgt);
	
	List<Tree> findByTgt(Trace t, String tgt);

}
