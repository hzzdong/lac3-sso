package com.linkallcloud.sso.service;

import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.Black;

public interface IBlackService extends IService<Black> {

	Black fetchExistBlack(Trace t, Black entity);

	List<Black> fetchExistBlacks(Trace t, Black entity);

	boolean blacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

	boolean unBlacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av);

}
