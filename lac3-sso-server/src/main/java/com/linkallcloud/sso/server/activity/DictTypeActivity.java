package com.linkallcloud.sso.server.activity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseTreeActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.sso.activity.IDictTypeActivity;
import com.linkallcloud.sso.domain.Dict;
import com.linkallcloud.sso.domain.DictType;
import com.linkallcloud.sso.server.dao.IDictDao;
import com.linkallcloud.sso.server.dao.IDictTypeDao;

@Component
public class DictTypeActivity extends BaseTreeActivity<DictType, IDictTypeDao> implements IDictTypeActivity {

	@Autowired
	private IDictTypeDao dictTypeDao;

	@Autowired
	private IDictDao dictDao;

	@Override
	public List<Tree> getTreeNodes(Trace t, boolean valid) {
		List<Tree> result = super.getTreeNodes(t, valid);
		Tree root = new Tree("DICT_TYPE_ROOT", null, "所有数据字典分类");
		root.setOpen(true);
		result = Trees.assembleChildren2Parent(result, root);
		return result;
	}

	@Override
	public Tree getTree(Trace t, boolean valid) {
		Tree tree = super.getTree(t, valid);
		tree.setOpen(true);
		tree.setId("DICT_TYPE_ROOT");
		tree.setName("所有数据字典分类");
		return tree;
	}

	@Override
	public DictType fetchByGovCode(Trace t, String dictTypeGovCode) {
		return dao().fetchByGovCode(t, dictTypeGovCode);
	}

	@Override
	public Tree getDictTypeTreeWithDictsById(Trace t, Long dictTypeId) {
		Tree root = null;
		DictType dt = dao().fetchById(t, dictTypeId);
		if (dt != null) {
			root = dt.toTreeNode();
			// root.setpId(null);

			List<DictType> children = dao().getChildrenByTopParentId(t, dt.getId());
			if (children != null && !children.isEmpty()) {
				Trees.assembleDomain2Tree(root, children);
			}

			List<Dict> dicts = dictDao.getDictsByTopTypeId(t, dt.getId());
			if (dicts != null && !dicts.isEmpty()) {
				Trees.assembleDomain2Tree(root, dicts);
			}
		}
		root.setpId(null);
		return root;
	}

	@Override
	public Tree getDirectDictsByTypeId(Trace t, Long dictTypeId) {
		Tree root = null;
		DictType dt = dao().fetchById(t, dictTypeId);
		if (dt != null) {
			root = dt.toTreeNode();
			// root.setpId(null);

			List<Dict> dicts = dictDao.getDictsByTypeId(t, dt.getId());
			if (dicts != null && !dicts.isEmpty()) {
				Trees.assembleDomain2Tree(root, dicts);
			}
		}
		root.setpId(null);
		return root;
	}

	@Override
	public IDictTypeDao dao() {
		return dictTypeDao;
	}

	@Override
	protected void before(Trace t, boolean isNew, DictType entity) {
		super.before(t, isNew, entity);
		if (isNew) {
			if (entity.isTopParent()) {
				entity.setTopParentId(null);
			} else {
				DictType parent = dao().fetchById(t, entity.getParentId());
				if (parent != null) {
					Long topParentId = Long.valueOf(parent.getCode().toString().split(parent.codeTag())[0]);
					entity.setTopParentId(topParentId);
				}
			}
		}
	}

}
