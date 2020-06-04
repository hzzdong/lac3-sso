package com.linkallcloud.sso.manager;

import java.util.Map;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.Black;
import com.linkallcloud.sso.exception.BlackListException;

public interface IBlackManager extends IManager<Black> {

	Black fetchBlack(Trace t, int type, String blackTarget);

	boolean blacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

	boolean unBlacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);
	
	public void check(Trace t, int appClazz, String blackTarget) throws BlackListException;

}
