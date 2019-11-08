package com.linkallcloud.sso.manager;

import java.util.Map;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.Black;

public interface IBlackManager extends IManager<Black> {
	
	boolean blacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

	boolean unBlacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

}
