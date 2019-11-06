package com.linkallcloud.sso.server.dao;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.sso.domain.Customer;

public interface ICustomerDao extends IDao<Customer> {

	Customer fetchByCode(@Param("t") Trace t, @Param("code") String code);

	Customer fetchByAccount(@Param("t") Trace t, @Param("account") String account);

    int updateBalance(@Param("customerId") Long customerId, @Param("fee") int fee);

    void deductionFee(@Param("id") Long id, @Param("fee") Long fee);
}
