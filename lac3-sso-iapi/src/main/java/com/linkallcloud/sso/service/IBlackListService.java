package com.linkallcloud.sso.service;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.sso.domain.BlackList;
import com.linkallcloud.sso.excel.BalckListPoiEntity;
import com.linkallcloud.sso.excel.BlackListPoiLog;

import java.util.List;

public interface IBlackListService extends IService<BlackList> {

    List<BlackListPoiLog> insertBatchBlackList(Trace t, List<BalckListPoiEntity> blackEntityList);
}
