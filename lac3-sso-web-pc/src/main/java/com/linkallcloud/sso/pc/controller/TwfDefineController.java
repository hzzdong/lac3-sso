package com.linkallcloud.sso.pc.controller;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.sso.domain.TwfDefine;
import com.linkallcloud.sso.manager.ITwfDefineManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/TwfDefine", method = RequestMethod.POST)
@Module(name = "流程定义")
public class TwfDefineController extends BaseLController<TwfDefine, ITwfDefineManager> {
    private static Log log = Logs.get();

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private ITwfDefineManager twfDefineManager;


    @Override
    protected ITwfDefineManager manager() {
        return twfDefineManager;
    }

    @RequestMapping(value = {"/add2"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String add2(Trace t, ModelMap modelMap, AppVisitor av) {
        return "page/TwfDefine/edit2";
    }

    @RequestMapping(value = {"/edit2"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String edit2(@RequestParam("id") Long id,
                        @RequestParam("uuid") String uuid, Trace t, ModelMap modelMap, AppVisitor av) {
        modelMap.put("id", id);
        modelMap.put("uuid", uuid);
        return "page/TwfDefine/edit2";
    }

    @RequestMapping(
            value = {"/list"},
            method = {RequestMethod.GET}
    )
    public String list(Trace t, ModelMap modelMap, AppVisitor av) {
        return "page/TwfDefine/list";
    }

    @RequestMapping(
            value = {"/main2"},
            method = {RequestMethod.GET}
    )
    public String main2(Trace t, ModelMap modelMap, AppVisitor av) {
        return "page/TwfDefine/main2";
    }

    @RequestMapping(
            value = {"/find"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result<Object> find(@RequestBody WebQuery webQuery, Trace t, AppVisitor av) {
        try {
            List<TwfDefine> list = manager().find(t, webQuery.toQuery());
            Result datatablesListResult = new Result(list);
            return new Result(datatablesListResult);
        } catch (Throwable var5) {
            return Exceptions.makeErrorResult(var5);
        }
    }

}
