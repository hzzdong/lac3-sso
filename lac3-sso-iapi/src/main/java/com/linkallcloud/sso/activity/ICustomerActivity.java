package com.linkallcloud.sso.activity;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Customer;

public interface ICustomerActivity extends IActivity<Customer> {
	
	Customer fetchByCode(Trace t, String code);

    Customer fetchByAccount(Trace t, String account);

    Customer loginValidate(Trace t, String loginName, String password);

    boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd);

}
