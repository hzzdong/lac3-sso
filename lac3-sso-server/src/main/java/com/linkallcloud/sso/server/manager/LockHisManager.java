package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.LockHis;
import com.linkallcloud.sso.manager.ILockHisManager;
import com.linkallcloud.sso.service.ILockHisService;

@DubboService(interfaceClass = ILockHisManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "锁/解锁历史")
public class LockHisManager extends BaseManager<LockHis, ILockHisService> implements ILockHisManager {

    @Autowired
    private ILockHisService lockHisService;

    @Override
    protected ILockHisService service() {
        return lockHisService;
    }

}
