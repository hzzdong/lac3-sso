package com.linkallcloud.sso.pc.controller;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.sso.domain.AppLoginHis;
import com.linkallcloud.sso.manager.IAppLoginHisManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/AppLoginHis", method = RequestMethod.POST)
@Module(name = "应用登录日志")
public class AppLoginHisController extends BaseLController<AppLoginHis, IAppLoginHisManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IAppLoginHisManager appLoginHisManager;

    @Override
    protected IAppLoginHisManager manager() {
        return appLoginHisManager;
    }
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> list(@RequestParam(value = "ut") String tgt, Trace t, AppVisitor av) {
		List<Tree> result = manager().findByTgt(t, tgt);
		return new Result<List<Tree>>(result);
	}

}
