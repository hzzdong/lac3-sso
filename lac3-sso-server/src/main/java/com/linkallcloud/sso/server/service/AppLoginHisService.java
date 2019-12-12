package com.linkallcloud.sso.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.IAppLoginHisActivity;
import com.linkallcloud.sso.domain.AppLoginHis;
import com.linkallcloud.sso.service.IAppLoginHisService;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppLoginHisService extends BaseService<AppLoginHis, IAppLoginHisActivity> implements IAppLoginHisService {

	@Autowired
	private IAppLoginHisActivity appLoginHisActivity;

	@Override
	public IAppLoginHisActivity activity() {
		return appLoginHisActivity;
	}

	@Override
	public List<Tree> findByTgt(Trace t, String tgt) {
		List<AppLoginHis> list = activity().findEntitiesByTgt(t, tgt);
		List<Tree> result = new ArrayList<Tree>();
		if (list != null && !list.isEmpty()) {
			CopyOnWriteArrayList<Tree> nodes = new CopyOnWriteArrayList<>();
			for (AppLoginHis lh : list) {
				Tree lhNode = lh.toTreeNode();
				if (Strings.isBlank(lh.getPpgt())) {// st auth
					result.add(lhNode);
				} else {
					nodes.add(lhNode);
				}
			}
			assemblelAppLoginHisTree(t, result, nodes);
		}
		return result;
	}

	private void assemblelAppLoginHisTree(Trace t, List<Tree> ts, CopyOnWriteArrayList<Tree> nodes) {
		if (ts == null || ts.isEmpty() || nodes == null || nodes.isEmpty()) {
			return;
		}
		for (Tree parent : ts) {
			String pgt = (String) parent.getAttributes().get("pgt");
			if (!Strings.isBlank(pgt)) {// 下面可能有
				for (Tree child : nodes) {
					String ppgt = (String) child.getAttributes().get("ppgt");
					if (!Strings.isBlank(ppgt) && pgt.equals(ppgt)) {
						child.setpId(parent.getId());
						parent.addChild(child);
						nodes.remove(child);
					}
				}
				if (parent.getChildren() != null && !parent.getChildren().isEmpty() && nodes != null
						&& !nodes.isEmpty()) {
					assemblelAppLoginHisTree(t, parent.getChildren(), nodes);
				}
			}
		}
	}

	@Override
	public List<AppLoginHis> findEntitiesByTgt(Trace t, String tgt) {
		return activity().findEntitiesByTgt(t, tgt);
	}

}
