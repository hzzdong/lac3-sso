package com.linkallcloud.sso.server.manager;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.sso.domain.Customer;
import com.linkallcloud.sso.manager.ICustomerManager;
import com.linkallcloud.sso.service.ICustomerService;

@Service(interfaceClass = ICustomerManager.class, version = "1.0.0")
@Component
@Module(name = "客户信息")
public class CustomerManager extends BaseManager<Customer, ICustomerService> implements ICustomerManager {

    @Autowired
    private ICustomerService customerService;

    @Override
    protected ICustomerService service() {
        return customerService;
    }

    @Override
    public Customer fetchByCode(Trace t, String code) {
        return service().fetchByCode(t, code);
    }

    @Override
    public Customer fetchByAccount(Trace t, String account) {
        return service().fetchByAccount(t, account);
    }

    @Override
    public Customer loginValidate(Trace t, String loginName, String password) {
        return service().loginValidate(t, loginName, password);
    }

    @Override
    public boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd) {
        return service().updatePassword(t, userId, userUuid, oldPwd, newPwd);
    }

}
