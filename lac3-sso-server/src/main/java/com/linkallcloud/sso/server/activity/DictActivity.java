package com.linkallcloud.sso.server.activity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseTreeActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.activity.IDictActivity;
import com.linkallcloud.sso.domain.Dict;
import com.linkallcloud.sso.domain.DictType;
import com.linkallcloud.sso.exception.ArgException;
import com.linkallcloud.sso.server.dao.IDictDao;
import com.linkallcloud.sso.server.dao.IDictTypeDao;

@Component
public class DictActivity extends BaseTreeActivity<Dict, IDictDao> implements IDictActivity {

	@Autowired
	private IDictDao dictDao;

	@Autowired
	private IDictTypeDao dictTypeDao;

	@Override
	public List<Dict> findDictsByDictTypeId(Trace t, Long dictTypeId) {
		return dao().getDictsByTypeId(t, dictTypeId);
	}

	@Override
	public IDictDao dao() {
		return dictDao;
	}

	@Override
	public List<Dict> findDictsByDictTopTypeId(Trace t, Long topDictTypeId) {
		return dao().getDictsByTopTypeId(t, topDictTypeId);
	}

	@Override
	protected void treeBefore(Trace t, boolean isNew, Dict entity) {
		if (isNew) {
			if (entity.getParentId() == null || entity.getParentId() <= 0L) {
				throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "parentId参数错误。");
			}

			if (!Strings.isBlank(entity.getGovCode())) {
				Dict dbEntity = dao().fetchByGovCode(t, entity.getGovCode());
				if (dbEntity != null) {
					throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "已经存在相同govCode的节点：" + entity.getGovCode());
				}
			}

			DictType parent = dictTypeDao.fetchById(t, entity.getParentId());
			Long topParentId = Long.valueOf(parent.getCode().toString().split(parent.codeTag())[0]);
			entity.setTopTypeId(topParentId);
		}
	}

	@Override
	protected void treeAfter(Trace t, boolean isNew, Dict entity) {

	}

}
