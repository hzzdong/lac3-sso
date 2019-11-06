package com.linkallcloud.sso.server.activity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.sso.activity.ICustomerActivity;
import com.linkallcloud.sso.domain.Customer;
import com.linkallcloud.sso.exception.AccountException;
import com.linkallcloud.sso.server.dao.ICustomerDao;

@Component
public class CustomerActivity extends BaseActivity<Customer, ICustomerDao> implements ICustomerActivity {

	@Autowired
	private ICustomerDao customerDao;

	@Override
	public ICustomerDao dao() {
		return customerDao;
	}

	@Override
	public Customer fetchByAccount(Trace t, String account) {
		return dao().fetchByAccount(t, account);
	}

	@Override
	public Customer fetchByCode(Trace t, String code) {
		return dao().fetchByCode(t, code);
	}

	@Override
	public Customer loginValidate(Trace t, String loginName, String password) {
		Customer company = dao().fetchByAccount(t, loginName);
		if (company != null) {
			if (Securities.password4Md5Src(password, company.getSalt()).equals(company.getPasswd())) {
				return company;
			}
		}
		return null;
	}

	@Override
	protected void before(Trace t, boolean isNew, Customer entity) {
		if (exist(t, entity, "adminAccount", entity.getAdminAccount())) {
			throw new AccountException(Exceptions.CODE_ERROR_AUTH_ACCOUNT, "已经存在相同账号的用户！");
		}

		if (isNew) {
			entity.setUuid(entity.generateUuid());
			entity.setSalt(entity.generateUuid());
			entity.setPasswd(Securities.password4Md5Src(entity.getPasswd(), entity.getSalt()));

			entity.setCreateTime(new Date());
		} else {
			entity.setUpdateTime(new Date());
			if (!Strings.isBlank(entity.getPasswd())) {
				entity.setSalt(entity.generateUuid());
				entity.setPasswd(Securities.password4Md5Src(entity.getPasswd(), entity.getSalt()));
			}
		}
	}

	@Transactional(readOnly = false)
	@Override
	public boolean updatePassword(Trace t, Long userId, String userUuid, String oldPwd, String newPwd) {
		Customer entity = this.fetchByIdUuid(t, userId, userUuid);
		if (entity != null) {
			if (Securities.validePassword4Md5Src(oldPwd, entity.getSalt(), entity.getPasswd())) {
				entity.setSalt(entity.generateUuid());
				entity.setPasswd(Securities.password4Md5Src(newPwd, entity.getSalt()));
				int rows = dao().update(t, entity);
				if (rows > 0) {
					// log.debug("update客户登录密码成功，" + t.getTid() + ", id:" + entity.getId());
				} else {
					log.error("update客户登录密码失败，" + t.getTid() + ", id:" + entity.getId());
				}
				return retBool(rows);
			}
		}
		return false;
	}

}
