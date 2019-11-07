package com.linkallcloud.sso.pc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.enums.LockType;

@Controller
@RequestMapping(value = "/IpLock", method = RequestMethod.POST)
@Module(name = "Ip锁")
public class IpLockController extends LockController {

	@Override
	protected LockType getLockType() {
		return LockType.IpLock;
	}
	
	@Override
	protected String getMainPage() {
		return "page/IpLock/main";
	}

	@Override
	protected String getEditPage() {
		return "page/IpLock/edit";
	}
	
	@Override
	protected String getViewPage() {
		return "page/IpLock/view";
	}

	@Override
	protected String getUnLockPage() {
		return "page/IpLock/unLock";
	}

	@Override
	protected String getLocksPage() {
		return "page/IpLock/locks";
	}

	@Override
	protected String getunLocksPage() {
		return "page/IpLock/unLocks";
	}

}
