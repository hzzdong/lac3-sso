package com.linkallcloud.sso.pc.controller;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.sso.domain.SysSetup;
import com.linkallcloud.sso.manager.ISysSetupManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/SysSetup", method = RequestMethod.POST)
@Module(name = "系统设置")
public class SysSetupController extends BaseLController<SysSetup, ISysSetupManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private ISysSetupManager sysSetupManager;

    @Override
    protected ISysSetupManager manager() {
        return sysSetupManager;
    }

}
