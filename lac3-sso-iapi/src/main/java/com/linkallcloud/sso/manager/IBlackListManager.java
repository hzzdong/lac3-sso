package com.linkallcloud.sso.manager;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.BlackList;
import com.linkallcloud.sso.excel.BalckListPoiEntity;
import com.linkallcloud.sso.excel.BlackListPoiLog;

import java.util.List;

/**
 * @author 孙宇冰
 * @version V1.0
 * @Package com.linkallcloud.vsms.iapi
 * @date 2019/9/3 14:41
 */
public interface IBlackListManager extends IManager<BlackList> {

    List<BlackListPoiLog> insertBatchBlackList(Trace t, List<BalckListPoiEntity> blackEntityList);

}
