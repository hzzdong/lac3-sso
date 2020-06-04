package com.linkallcloud.sso.manager;

import java.util.Map;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.Lock;
import com.linkallcloud.sso.exception.LockException;

public interface ILockManager extends IManager<Lock> {

	boolean locks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

	boolean unLocks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

	void check(Trace t, int appClazz, String lockedTarget) throws LockException;

	void dealAutoLock(Trace t, boolean success, int appClazz, String account, String ip, String remark);

}
