package com.linkallcloud.sso.pc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.enums.LockBlackType;

@Controller
@RequestMapping(value = "/AccountLock", method = RequestMethod.POST)
@Module(name = "账号锁")
public class AccountLockController extends LockController {

	@Override
	protected LockBlackType getLockType() {
		return LockBlackType.Account;
	}

	@Override
	protected String getMainPage() {
		return "page/AccountLock/main";
	}

	@Override
	protected String getEditPage() {
		return "page/AccountLock/edit";
	}

	@Override
	protected String getViewPage() {
		return "page/AccountLock/view";
	}

	@Override
	protected String getUnLockPage() {
		return "page/AccountLock/unLock";
	}

	@Override
	protected String getLocksPage() {
		return "page/AccountLock/locks";
	}

	@Override
	protected String getunLocksPage() {
		return "page/AccountLock/unLocks";
	}

}
