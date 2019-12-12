package com.linkallcloud.sso.service;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.LoginHis;

public interface ILoginHisService extends IService<LoginHis> {
	
	void logout(Trace t, String md5Tgt);
	void logout(Trace t, Long id);
	LoginHis fetchByTgt(Trace t, String md5Tgt);

}
