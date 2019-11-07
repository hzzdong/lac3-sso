package com.linkallcloud.sso.manager;

import java.util.Map;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.Lock;

public interface ILockManager extends IManager<Lock> {

	boolean locks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

	boolean unLocks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

}
