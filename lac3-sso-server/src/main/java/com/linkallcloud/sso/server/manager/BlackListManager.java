package com.linkallcloud.sso.server.manager;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.BlackList;
import com.linkallcloud.sso.excel.BalckListPoiEntity;
import com.linkallcloud.sso.excel.BlackListPoiLog;
import com.linkallcloud.sso.manager.IBlackListManager;
import com.linkallcloud.sso.service.IBlackListService;

@Service(interfaceClass = IBlackListManager.class, version = "1.0.0")
@Component
@Module(name = "黑名单")
public class BlackListManager extends BaseManager<BlackList, IBlackListService> implements IBlackListManager {

    @Autowired
    private IBlackListService blackListService;

    @Override
    protected IBlackListService service() {
        return blackListService;
    }

    @Override
    public List<BlackListPoiLog> insertBatchBlackList(Trace t, List<BalckListPoiEntity> blackEntityList) {
        return service().insertBatchBlackList(t, blackEntityList);
    }
}
