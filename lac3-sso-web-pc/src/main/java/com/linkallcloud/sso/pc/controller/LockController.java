package com.linkallcloud.sso.pc.controller;

import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;
import com.linkallcloud.sso.domain.Lock;
import com.linkallcloud.sso.enums.LockReson;
import com.linkallcloud.sso.enums.LockStatus;
import com.linkallcloud.sso.enums.LockBlackType;
import com.linkallcloud.sso.manager.ILockManager;
import com.linkallcloud.web.controller.BaseLController;

public abstract class LockController extends BaseLController<Lock, ILockManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private ILockManager lockManager;

	@Override
	protected ILockManager manager() {
		return lockManager;
	}

	protected abstract LockBlackType getLockType();

	@Override
	protected Page<Lock> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
		webPage.addRule(new StringRuleDescriptor("type", "eq", getLockType().getCode(), "I"));
		return super.doFindPage(webPage, t, av);
	}

	@Override
	protected Lock doGet(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
		LockBlackType type = getLockType();
		Lock entity = super.doGet(parentId, parentClass, id, uuid, t, av);
		if (id == null) {
			entity.setType(type.getCode());
		}
		return entity;
	}

	@Override
	protected String doView(Long id, String uuid, ModelMap modelMap, Trace t, AppVisitor av) {
		Lock entity = manager().fetchByIdUuid(t, id, uuid);
		modelMap.put("entity", entity);
		return super.doView(id, uuid, modelMap, t, av);
	}

	@Override
	protected Lock doSave(Lock entity, Trace t, AppVisitor av) {
		entity.setType(getLockType().getCode());
		entity.setStatus(LockStatus.Lock.getCode());
		entity.setReason(LockReson.LockByHand.getCode());
		entity.setCount(1);
		entity.setLockedTime(new Date());
		entity.setOperator(av != null ? av.name() : "");
		return super.doSave(entity, t, av);
	}

	/*- 单个解锁 *************************************************************************************/
	@RequestMapping(value = "/unLock", method = RequestMethod.GET)
	public String unLock(@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t,
			ModelMap modelMap, AppVisitor av) {
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);
		return getUnLockPage();
	}

	protected abstract String getUnLockPage();

	@RequestMapping(value = "/unLockDo", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> unLockDo(@RequestBody @Valid Lock entity, Trace t, AppVisitor av) {
		if (!checkReferer(true)) {
			return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		Lock domain = doUnLockDo(entity, t, av);
		return new Result<Object>(domain);
	}

	private Lock doUnLockDo(@Valid Lock entity, Trace t, AppVisitor av) {
		entity.setType(getLockType().getCode());
		entity.setStatus(LockStatus.UnLock.getCode());
		entity.setReason(LockReson.UnLockByHand.getCode());
		entity.setLockedTime(new Date());
		entity.setOperator(av != null ? av.name() : "");
		entity.setStatus(LockStatus.UnLock.getCode());
		return super.doSave(entity, t, av);
	}

	/*- 批量锁定 *************************************************************************************/
	@RequestMapping(value = "/locks", method = RequestMethod.GET)
	public String locks(Trace t) {
		return getLocksPage();
	}

	protected abstract String getLocksPage();

	@RequestMapping(value = "/locksDo", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> locksDo(@RequestParam(value = "remark") String remark,
			@RequestBody Map<String, Long> uuidIds, Trace t, AppVisitor av) {
		if (!checkReferer(true)) {
			return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		boolean success = manager().locks(t, uuidIds, remark, av);
		return new Result<Object>(!success, "locks", "批量锁定失败");
	}

	/*- 批量解锁 ************************************************************************************/
	@RequestMapping(value = "/unLocks", method = RequestMethod.GET)
	public String unLocks(Trace t) {
		return getunLocksPage();
	}

	protected abstract String getunLocksPage();

	@RequestMapping(value = "/unLocksDo", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> unLocksDo(@RequestParam(value = "remark") String remark,
			@RequestBody Map<String, Long> uuidIds, Trace t, AppVisitor av) {
		if (!checkReferer(true)) {
			return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		boolean success = manager().unLocks(t, uuidIds, remark, av);
		return new Result<Object>(!success, "unLocks", "批量锁定失败");
	}

}
