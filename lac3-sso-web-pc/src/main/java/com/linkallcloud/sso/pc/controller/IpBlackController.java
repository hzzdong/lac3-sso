package com.linkallcloud.sso.pc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.sso.enums.LockBlackType;

@Controller
@RequestMapping(value = "/IpBlack", method = RequestMethod.POST)
@Module(name = "黑名单")
public class IpBlackController extends BlackController {

	@Override
	protected LockBlackType getBlackType() {
		return LockBlackType.Ip;
	}
	
	@Override
	protected String getMainPage() {
		return "page/IpBlack/main";
	}

	@Override
	protected String getEditPage() {
		return "page/IpBlack/edit";
	}

	@Override
	protected String getViewPage() {
		return "page/IpBlack/view";
	}

	@Override
	protected String getUnBlackPage() {
		return "page/IpBlack/unBlack";
	}

	@Override
	protected String getBlacksPage() {
		return "page/IpBlack/blacks";
	}

	@Override
	protected String getunBlacksPage() {
		return "page/IpBlack/unBlacks";
	}

}
