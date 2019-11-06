package com.linkallcloud.sso.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.IBlackListActivity;
import com.linkallcloud.sso.domain.BlackList;
import com.linkallcloud.sso.excel.BalckListPoiEntity;
import com.linkallcloud.sso.excel.BlackListPoiLog;
import com.linkallcloud.sso.service.IBlackListService;

@Service
@Transactional(readOnly = true)
public class BlcakListService extends BaseService<BlackList, IBlackListActivity> implements IBlackListService {

	@Autowired
	private IBlackListActivity blackListActivity;

	@Override
	public IBlackListActivity activity() {
		return blackListActivity;
	}

	@Transactional(readOnly = false)
	@Override
	public List<BlackListPoiLog> insertBatchBlackList(Trace t, List<BalckListPoiEntity> blackEntityList) {
		return activity().insertBatchBlackList(t, blackEntityList);
	}

}
