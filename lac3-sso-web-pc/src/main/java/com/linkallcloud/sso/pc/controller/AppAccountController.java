package com.linkallcloud.sso.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.domain.AppAccount;
import com.linkallcloud.sso.manager.IAppAccountManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/AppAccount", method = RequestMethod.POST)
@Module(name = "应用账号")
public class AppAccountController extends BaseLController<AppAccount, IAppAccountManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IAppAccountManager appAccountManager;

    @Override
    protected IAppAccountManager manager() {
        return appAccountManager;
    }

}
