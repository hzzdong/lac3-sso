package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.SysSetup;
import com.linkallcloud.sso.manager.ISysSetupManager;
import com.linkallcloud.sso.service.ISysSetupService;

@Service(interfaceClass = ISysSetupManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "系统设置")
public class SysSetupManager extends BaseManager<SysSetup, ISysSetupService> implements ISysSetupManager {

	@Autowired
	private ISysSetupService sysSetupService;

	@Override
	protected ISysSetupService service() {
		return sysSetupService;
	}

	@Override
	public SysSetup fetchByCode(Trace t, String code) {
		return service().fetchByCode(t, code);
	}

//	@Override
//	public SysSetup getErrorTimesCount4AccountLock(Trace t) {
//		return service().getErrorTimesCount4AccountLock(t);
//	}
//
//	@Override
//	public SysSetup getPeriod4AccountLock(Trace t) {
//		return service().getPeriod4AccountLock(t);
//	}
//
//	@Override
//	public SysSetup getLockTimesCount4AccountLock(Trace t) {
//		return service().getLockTimesCount4AccountLock(t);
//	}
//
//	@Override
//	public SysSetup getErrorTimesCount4IpLock(Trace t) {
//		return service().getErrorTimesCount4IpLock(t);
//	}
//
//	@Override
//	public SysSetup getPeriod4IpLock(Trace t) {
//		return service().getPeriod4IpLock(t);
//	}
//
//	@Override
//	public SysSetup getLockTimesCount4IpLock(Trace t) {
//		return service().getLockTimesCount4IpLock(t);
//	}

}
