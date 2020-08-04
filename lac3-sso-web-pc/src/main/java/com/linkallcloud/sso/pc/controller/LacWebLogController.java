package com.linkallcloud.sso.pc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.busilog.LogExport;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.sso.domain.LacWebLog;
import com.linkallcloud.sso.manager.ILacWebLogManager;
import com.linkallcloud.sso.pc.utils.FileUtil;
import com.linkallcloud.web.controller.BaseLacLogController;

@Controller
@RequestMapping(value = "/log", method = RequestMethod.POST)
public class LacWebLogController extends BaseLacLogController<LacWebLog, ILacWebLogManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private ILacWebLogManager lacWebLogManager;

	@Override
	protected ILacWebLogManager manager() {
		return lacWebLogManager;
	}

	@RequestMapping(value = "export", method = RequestMethod.POST)
	public void export(HttpServletResponse response,
			@RequestParam(value = "searchCnds", required = false) String searchCnds, Trace t) {
		String fileName = "操作日志_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".xls";
		Query query = JSON.parseObject(searchCnds, Query.class);
		List<LacWebLog> logs = manager().find(t, query);
		List<LogExport> list = new ArrayList<LogExport>();
		if (logs != null && logs.size() > 0) {
			for (LacWebLog blog : logs) {
				LogExport le = new LogExport(blog);
				list.add(le);
			}
		}
		FileUtil.exportExcel(list, null, "操作日志", LogExport.class, fileName, response);
	}

}
