package com.linkallcloud.sso.service;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.Lock;
import com.linkallcloud.sso.dto.LockConfig;
import com.linkallcloud.sso.exception.LockException;

public interface ILockService extends IService<Lock> {
	
	Lock fetchExistLock(Trace t, Integer appClazz, Integer type, String lockedTarget, Integer status);
	List<Lock> findExistLocks(Trace t, Integer appClazz, Integer type, String lockedTarget, Integer status);
	
	boolean locks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);
	boolean unLocks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);
	
	void load2Cache(Trace t);
	void autoUnLockBlack(Trace t, LockConfig config);
	void check(Trace t, int appClazz, String lockedTarget) throws LockException;

	void dealAuthAutoLock(Trace t, boolean success, int appClazz, String account, String ip, String remark, LockConfig config);

}
