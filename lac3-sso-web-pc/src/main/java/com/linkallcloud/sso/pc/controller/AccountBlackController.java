package com.linkallcloud.sso.pc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.enums.LockBlackType;

@Controller
@RequestMapping(value = "/AccountBlack", method = RequestMethod.POST)
@Module(name = "黑名单")
public class AccountBlackController extends BlackController {

	@Override
	protected LockBlackType getBlackType() {
		return LockBlackType.Account;
	}
	
	@Override
	protected String getMainPage() {
		return "page/AccountBlack/main";
	}

	@Override
	protected String getEditPage() {
		return "page/AccountBlack/edit";
	}

	@Override
	protected String getViewPage() {
		return "page/AccountBlack/view";
	}

	@Override
	protected String getUnBlackPage() {
		return "page/AccountBlack/unBlack";
	}

	@Override
	protected String getBlacksPage() {
		return "page/AccountBlack/blacks";
	}

	@Override
	protected String getunBlacksPage() {
		return "page/AccountBlack/unBlacks";
	}

}
