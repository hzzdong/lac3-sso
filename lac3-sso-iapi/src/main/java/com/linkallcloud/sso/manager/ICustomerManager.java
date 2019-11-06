package com.linkallcloud.sso.manager;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.sso.domain.Customer;

public interface ICustomerManager extends IManager<Customer> {

    Customer fetchByCode(Trace t, String code);

    Customer fetchByAccount(Trace t, String account);

    Customer loginValidate(Trace t, String loginName, String password);

    boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd);
}
