package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.BlackHis;
import com.linkallcloud.sso.manager.IBlackHisManager;
import com.linkallcloud.sso.service.IBlackHisService;

@DubboService(interfaceClass = IBlackHisManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "黑名单历史")
public class BlackHisManager extends BaseManager<BlackHis, IBlackHisService> implements IBlackHisManager {

    @Autowired
    private IBlackHisService blackHisService;

    @Override
    protected IBlackHisService service() {
        return blackHisService;
    }

}
