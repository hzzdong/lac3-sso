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
import com.linkallcloud.sso.domain.Black;
import com.linkallcloud.sso.enums.BlackReson;
import com.linkallcloud.sso.enums.BlackStatus;
import com.linkallcloud.sso.enums.LockBlackType;
import com.linkallcloud.sso.manager.IBlackManager;
import com.linkallcloud.web.controller.BaseLController;

public abstract class BlackController extends BaseLController<Black, IBlackManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IBlackManager blackManager;

    @Override
    protected IBlackManager manager() {
        return blackManager;
    }
    

	protected abstract LockBlackType getBlackType();

	@Override
	protected Page<Black> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
		webPage.addRule(new StringRuleDescriptor("type", "eq", getBlackType().getCode(), "I"));
		return super.doFindPage(webPage, t, av);
	}

	@Override
	protected Black doGet(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
		LockBlackType type = getBlackType();
		Black entity = super.doGet(parentId, parentClass, id, uuid, t, av);
		if (id == null) {
			entity.setType(type.getCode());
		}
		return entity;
	}

	@Override
	protected String doView(Long id, String uuid, ModelMap modelMap, Trace t, AppVisitor av) {
		Black entity = manager().fetchByIdUuid(t, id, uuid);
		modelMap.put("entity", entity);
		return super.doView(id, uuid, modelMap, t, av);
	}

	@Override
	protected Black doSave(Black entity, Trace t, AppVisitor av) {
		entity.setType(getBlackType().getCode());
		entity.setStatus(BlackStatus.Black.getCode());
		entity.setReason(BlackReson.BlackByHand.getCode());
		entity.setBlackTime(new Date());
		entity.setOperator(av != null ? av.getName() : "");
		return super.doSave(entity, t, av);
	}

	/*- 单个除黑 *************************************************************************************/
	@RequestMapping(value = "/unBlack", method = RequestMethod.GET)
	public String unBlack(@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t,
			ModelMap modelMap, AppVisitor av) {
		modelMap.put("id", id);
		modelMap.put("uuid", uuid);
		return getUnBlackPage();
	}

	protected abstract String getUnBlackPage();

	@RequestMapping(value = "/unBlackDo", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> unBlackDo(@RequestBody @Valid Black entity, Trace t, AppVisitor av) {
		if (!checkReferer(true)) {
			return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		Black domain = doUnBlackDo(entity, t, av);
		return new Result<Object>(domain);
	}

	private Black doUnBlackDo(@Valid Black entity, Trace t, AppVisitor av) {
		entity.setType(getBlackType().getCode());
		entity.setStatus(BlackStatus.UnBlack.getCode());
		entity.setReason(BlackReson.UnBlackByHand.getCode());
		entity.setBlackTime(new Date());
		entity.setOperator(av != null ? av.getName() : "");
		entity.setStatus(BlackStatus.UnBlack.getCode());
		return super.doSave(entity, t, av);
	}

	/*- 批量加黑 *************************************************************************************/
	@RequestMapping(value = "/blacks", method = RequestMethod.GET)
	public String blacks(Trace t) {
		return getBlacksPage();
	}

	protected abstract String getBlacksPage();

	@RequestMapping(value = "/blacksDo", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> blacksDo(@RequestParam(value = "remark") String remark,
			@RequestBody Map<String, Long> uuidIds, Trace t, AppVisitor av) {
		if (!checkReferer(true)) {
			return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		boolean success = manager().blacks(t, uuidIds, remark, av);
		return new Result<Object>(!success, "blacks", "批量加黑失败");
	}

	/*- 批量除黑 ************************************************************************************/
	@RequestMapping(value = "/unBlacks", method = RequestMethod.GET)
	public String unBlacks(Trace t) {
		return getunBlacksPage();
	}

	protected abstract String getunBlacksPage();

	@RequestMapping(value = "/unBlacksDo", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> unBlacksDo(@RequestParam(value = "remark") String remark,
			@RequestBody Map<String, Long> uuidIds, Trace t, AppVisitor av) {
		if (!checkReferer(true)) {
			return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		boolean success = manager().unBlacks(t, uuidIds, remark, av);
		return new Result<Object>(!success, "unBlacks", "批量除黑失败");
	}

}
