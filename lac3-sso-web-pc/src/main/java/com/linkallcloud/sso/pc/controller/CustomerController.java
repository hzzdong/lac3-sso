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
import com.linkallcloud.sso.domain.Customer;
import com.linkallcloud.sso.manager.ICustomerManager;
import com.linkallcloud.web.controller.BaseLController;

@Controller
@RequestMapping(value = "/Customer", method = RequestMethod.POST)
@Module(name = "客户")
//@RequirePermissions({ "vsms-customer" })
public class CustomerController extends BaseLController<Customer, ICustomerManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private ICustomerManager customerManager;

    @Override
    protected ICustomerManager manager() {
        return customerManager;
    }

    @Override
    public void addAreaCnd2Page(Page<Customer> page, AppVisitor av) {
    }

    @Override
    protected Customer doSave(Customer entity, Trace t, AppVisitor av) {
        entity = super.doSave(entity, t, av);
        entity = manager().fetchByIdUuid(t, entity.getId(), entity.getUuid());
        return entity;
    }

    @GetMapping("/listByAgentId")
    @ResponseBody
    public Result<List<Customer>> listByAgentId(@RequestParam("agentId") Long agentId, AppVisitor av) {
        Query query = new Query();
        query.addRule(new Equal("agentId", agentId));
        List<Customer> customerList = manager().find(new Trace(), query);
        return new Result<>(customerList);
    }

}
