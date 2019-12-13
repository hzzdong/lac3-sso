package com.linkallcloud.sso.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.IsNull;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.core.util.Dates;
import com.linkallcloud.sso.activity.ILoginHisActivity;
import com.linkallcloud.sso.domain.LoginHis;
import com.linkallcloud.sso.redis.ticket.RedisTicketGrantingTicketCache;
import com.linkallcloud.sso.service.ILoginHisService;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginHisService extends BaseService<LoginHis, ILoginHisActivity> implements ILoginHisService {

	@Autowired
	private ILoginHisActivity loginHisActivity;

	@Autowired
	private RedisTicketGrantingTicketCache redisTicketGrantingTicketCache;

	@Value("${lac.sso.tgt.timeout:7200}")
	private int tolerance;

	@Override
	public ILoginHisActivity activity() {
		return loginHisActivity;
	}

	@Override
	public void logout(Trace t, String md5Tgt) {
		activity().logout(t, md5Tgt);
	}

	@Override
	public void logout(Trace t, Long id) {
		activity().logout(t, id);
	}

	@Override
	public LoginHis fetchByTgt(Trace t, String md5Tgt) {
		return activity().fetchByTgt(t, md5Tgt);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean offline(Trace t, Long id, String uuid) {
		LoginHis ol = activity().fetchByIdUuid(t, id, uuid);
		if (ol != null && !Strings.isBlank(ol.getTgt())) {
			redisTicketGrantingTicketCache.remove(ol.getTgt());
		}

		activity().logout(t, id);
		return true;
	}

	@Transactional(readOnly = false)
	@Override
	public void markLoginTimeoutLogs(Trace t) {
		Query query = new Query();
		query.addRule(new IsNull("logoutTime"));
		List<LoginHis> onlines = activity().find(t, query);
		if (onlines != null && !onlines.isEmpty()) {
			for (LoginHis ol : onlines) {
				if (ol.isTimeout(tolerance)) {
					ol.setLogoutTime(Dates.addSecond(ol.getLoginTime(), tolerance));
					activity().update(t, ol);
				}
			}
		}
	}

}
