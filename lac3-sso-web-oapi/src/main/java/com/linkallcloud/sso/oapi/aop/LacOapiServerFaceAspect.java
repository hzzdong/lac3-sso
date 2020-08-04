package com.linkallcloud.sso.oapi.aop;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.security.MsgSignObject;
import com.linkallcloud.sso.oapi.kiss.um.ApplicationKiss;
import com.linkallcloud.sso.oapi.kiss.um.KhUserKiss;
import com.linkallcloud.sso.oapi.kiss.um.YwUserKiss;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.web.face.FaceAspect;
import com.linkallcloud.web.face.processor.PackageJsonProcessor;
import com.linkallcloud.web.face.processor.PackageXmlProcessor;
import com.linkallcloud.web.face.processor.SimpleJsonProcessor;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.session.SimpleSessionUser;

@Aspect
@Component
@Order(2)
public class LacOapiServerFaceAspect extends FaceAspect {

	@Autowired
	private ApplicationKiss applicationKiss;

	@Autowired
	private YwUserKiss ywUserKiss;

	@Autowired
	private KhUserKiss khUserKiss;

	@Pointcut("execution(public * com.linkallcloud.sso.oapi.face..*.*(..))")
	public void oapiface() {
	}

	@Around("oapiface()")
	@Override
	public Object checkFace(ProceedingJoinPoint joinPoint) throws Throwable {
		return super.checkFace(joinPoint);
	}

	@Override
	protected PackageJsonProcessor getPackageJsonProcessor() {
		PackageJsonProcessor pjp = new PackageJsonProcessor() {

			@Override
			protected SessionUser getSessionUserBySimpleSessionUser(SimpleSessionUser ssu) {
				if (ssu.getUserType().equals("Yw")) {
					return ywUserKiss.assembleSessionUser(new Trace(), ssu.getLoginName(), ssu.appCode(), ssu.getOrg());
				} else {
					return khUserKiss.assembleSessionUser(new Trace(), ssu.getLoginName(), ssu.appCode(), ssu.getOrg());
				}
			}

		};
		List<Application> apps = findApplications();
		if (apps != null && !apps.isEmpty()) {
			for (Application app : apps) {
				MsgSignObject mso = new MsgSignObject(app.getCode(), app.getSignatureAlg(), app.getSignatureKey(),
						app.getMessageEncAlg(), app.getMessageEncKey());
				mso.setTimeout(app.getTimeout() < 10 ? 10 : app.getTimeout());
				pjp.addMsgSignObject(app.getCode(), mso);
			}
		}
		return pjp;
	}

	@Override
	protected PackageXmlProcessor getPackageXmlProcessor() {
		return null;
	}

	private List<Application> findApplications() {
		return applicationKiss.find(new Trace(true), new ListFaceRequest());
	}

	@Override
	protected SimpleJsonProcessor getSimpleJsonProcessor() {
		return new SimpleJsonProcessor() {

			@Override
			protected SessionUser getSessionUserBySimpleSessionUser(SimpleSessionUser ssu) {
				if (ssu.getUserType().equals("Yw")) {
					return ywUserKiss.assembleSessionUser(new Trace(), ssu.getLoginName(), ssu.appCode(), ssu.getOrg());
				} else {
					return khUserKiss.assembleSessionUser(new Trace(), ssu.getLoginName(), ssu.appCode(), ssu.getOrg());
				}
			}

		};
	}

}
