package com.linkallcloud.sso.server.aop;

import com.linkallcloud.core.exception.BizExceptionAspect;
import com.linkallcloud.sso.exception.SsoException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-999)
public class LacExceptionAspect extends BizExceptionAspect<SsoException> {

    @Pointcut("execution(public * com.linkallcloud.sso.server.manager..*.*(..))")
    public void manager() {
    }

    @Pointcut("execution(* com.linkallcloud.core.manager.*.*(..))")
    public void manager2() {
    }

    @Override
    @Around("manager() || manager2()")
    public Object wrapException(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.wrapException(joinPoint);
    }

}
