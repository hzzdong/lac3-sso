package com.linkallcloud.sso.pc.aop;

import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.linkallcloud.sso.domain.LacWebLog;
import com.linkallcloud.sso.manager.ILacWebLogManager;
import com.linkallcloud.web.busilog.BusiWebLogAspect;

@Aspect
@Component
@Order(3)
public class LacWebLogAspect extends BusiWebLogAspect<LacWebLog, ILacWebLogManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private ILacWebLogManager lacWebLogManager;

	@Override
	protected ILacWebLogManager logService() {
		return lacWebLogManager;
	}

	// @Pointcut("@annotation(com.linkallcloud.core.busilog.annotation.WebLog)")
	@Pointcut("execution(public * com.linkallcloud.sso.pc.controller..*.*(..))")
	public void xfWebLog() {
	}

	@Pointcut("execution(* com.linkallcloud.web.controller.*.*(..))")
	public void webLog() {
	}

	@Override
	@Around("xfWebLog() || webLog()")
	public Object autoLog(ProceedingJoinPoint joinPoint) throws Throwable {
		return super.autoLog(joinPoint);
	}

}
