package com.linkallcloud.sso.server.manager;

import java.util.Map;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.Lock;
import com.linkallcloud.sso.exception.LockException;
import com.linkallcloud.sso.manager.ILockManager;
import com.linkallcloud.sso.server.configure.LockConfigure;
import com.linkallcloud.sso.service.ILockService;

@Service(interfaceClass = ILockManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "锁")
public class LockManager extends BaseManager<Lock, ILockService> implements ILockManager {

	@Autowired
	private ILockService lockService;

	@Autowired
	private LockConfigure lockConfigure;

	@Override
	protected ILockService service() {
		return lockService;
	}

	@Override
	public boolean locks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av) {
		return service().locks(t, uuidIds, remark, av);
	}

	@Override
	public boolean unLocks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av) {
		return service().unLocks(t, uuidIds, remark, av);
	}

	@Override
	public void check(Trace t, int appClazz, String lockedTarget) throws LockException {
		service().check(t, appClazz, lockedTarget);
	}

	@Override
	public void dealAutoLock(Trace t, boolean success, int appClazz, String account, String ip, String remark) {
		service().dealAuthAutoLock(t, success, appClazz, account, ip, remark, lockConfigure.loadLockConfig(t));
	}

}
