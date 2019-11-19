package com.linkallcloud.sso.service;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.Black;
import com.linkallcloud.sso.exception.BlackListException;

public interface IBlackService extends IService<Black> {
	
	Black fetchBlack(Trace t, Integer type, String blackTarget);

	Black fetchExistBlack(Trace t, Black entity);

	List<Black> findExistBlacks(Trace t, Integer type, String blackTarget);

	boolean blacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

	boolean unBlacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);
	
	public void load2Cache(Trace t);
	public void check(Trace t, String blackTarget) throws BlackListException;

}
