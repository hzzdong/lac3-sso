package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.SysSetup;
import com.linkallcloud.sso.manager.ISysSetupManager;
import com.linkallcloud.sso.service.ISysSetupService;

@Service(interfaceClass = ISysSetupManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "系统设置")
public class SysSetupManager extends BaseManager<SysSetup, ISysSetupService> implements ISysSetupManager {

    @Autowired
    private ISysSetupService sysSetupService;

    @Override
    protected ISysSetupService service() {
        return sysSetupService;
    }

}
