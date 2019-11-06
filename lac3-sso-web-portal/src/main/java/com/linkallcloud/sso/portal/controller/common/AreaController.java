package com.linkallcloud.sso.portal.controller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;
import com.linkallcloud.sso.portal.kiss.um.AreaKiss;
import com.linkallcloud.um.domain.sys.Area;

@Controller
@RequestMapping(value = "/Area", method = RequestMethod.POST)
@Module(name = "区域")
public class AreaController {

	@Autowired
	private AreaKiss areaKiss;

	/**
	 * 全国区域树
	 *
	 * @param t
	 * @param av
	 * @return
	 */
	@RequestMapping(value = "/loadTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> loadFullTreeOri(Trace t, AppVisitor av) {
		List<Tree> items = areaKiss.getTreeNodes(t, true);
		return new Result<>(items);
	}

	/**
	 * 地市级别以上区域树
	 *
	 * @param t
	 * @param av
	 * @return
	 */
	@RequestMapping(value = "/dsTree", method = RequestMethod.GET)
	public @ResponseBody Result<List<Tree>> dsTree(Trace t, AppVisitor av) {
		ListFaceRequest req = new ListFaceRequest();
		req.addRule(new StringRuleDescriptor("levelLt", "lt", 3, "I"));
		req.addRule(new StringRuleDescriptor("status", "ge", 0, "I"));
		List<Area> areas = areaKiss.find(t, req);

		Tree root = new Tree("0", null, "中华人民共和国");
		root.setOpen(true);
		List<Tree> trees = Trees.assembleDomain2List(root, areas);
		return new Result<>(trees);
	}

}
