package com.linkallcloud.sso.server.configure;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.sso.dto.LockConfig;
import com.linkallcloud.sso.service.IBlackService;
import com.linkallcloud.sso.service.ILockService;
import com.linkallcloud.sso.service.ILoginHisService;

@Component
@Order(1)
public class SSOServerRunner implements ApplicationRunner {
	private static Log log = Logs.get();

	@Autowired
	private IBlackService blackService;

	@Autowired
	private ILockService lockService;

	@Autowired
	private LockConfigure lockConfigure;
	
	@Autowired
	private ILoginHisService loginHisService;

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	private LockConfig config;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Trace t = new Trace(true);
		blackService.load2Cache(t);
		lockService.load2Cache(t);

		config = lockConfigure.loadLockConfig(t);
		autoUnlockExecute(t);
		
		markLoginTimeoutLogs(t);
	}

	private void markLoginTimeoutLogs(Trace t) {
		executor.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				try {
					loginHisService.markLoginTimeoutLogs(t);
				} catch (Throwable e) {
					log.error(e.getMessage(), e);
				}
			}

		}, 0, 10, TimeUnit.MINUTES);
	}

	private void autoUnlockExecute(Trace t) {
		executor.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				try {
					lockService.autoUnLockBlack(t, config);
				} catch (Throwable e) {
					log.error(e.getMessage(), e);
				}
			}

		}, 0, 2, TimeUnit.MINUTES);
	}


}
