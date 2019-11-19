package com.linkallcloud.sso.service;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.SysSetup;

public interface ISysSetupService extends IService<SysSetup> {
	
	SysSetup fetchByCode(Trace t, String code);
	
//	SysSetup getErrorTimesCount4AccountLock(Trace t);
//	SysSetup getPeriod4AccountLock(Trace t);
//	SysSetup getLockTimesCount4AccountLock(Trace t);
//	
//	SysSetup getErrorTimesCount4IpLock(Trace t);
//	SysSetup getPeriod4IpLock(Trace t);
//	SysSetup getLockTimesCount4IpLock(Trace t);

}
