package com.linkallcloud.sso.service;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.Lock;

public interface ILockService extends IService<Lock> {
	
	Lock fetchExistLock(Trace t, Lock entity);
	
	List<Lock> fetchExistLocks(Trace t, Lock entity);
	
	boolean locks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

	boolean unLocks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

}
