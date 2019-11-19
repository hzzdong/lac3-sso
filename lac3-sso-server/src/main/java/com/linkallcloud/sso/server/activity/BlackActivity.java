package com.linkallcloud.sso.server.activity;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.sso.activity.IBlackActivity;
import com.linkallcloud.sso.domain.Black;
import com.linkallcloud.sso.domain.BlackHis;
import com.linkallcloud.sso.enums.BlackStatus;
import com.linkallcloud.sso.server.dao.IBlackDao;
import com.linkallcloud.sso.server.dao.IBlackHisDao;

@Component
public class BlackActivity extends BaseActivity<Black, IBlackDao> implements IBlackActivity {

	@Autowired
	private IBlackDao blackDao;

	@Autowired
	private IBlackHisDao blackHisDao;

	@Override
	public IBlackDao dao() {
		return blackDao;
	}

	public void saveBlackHis(Trace t, Black entity) {
		BlackHis his = new BlackHis(entity);
		blackHisDao.insert(t, his);
	}

	@Override
	public Long insert(Trace t, Black entity) {
		Long id = super.insert(t, entity);
		entity.setId(id);
		saveBlackHis(t, entity);
		return id;
	}

	@Override
	public boolean update(Trace t, Black entity) {
		boolean ret = super.update(t, entity);
		saveBlackHis(t, entity);
		return ret;
	}

	@Override
	public void autoBlack(Trace t, Black black) {
		Black dbEntity = fetchExistBlack(t, black.getType(), black.getBlackTarget(), null);
		if (dbEntity != null) {
			dbEntity.setBlackTime(new Date());
			dbEntity.setOperator(black.getOperator());
			dbEntity.setReason(black.getReason());
			dbEntity.setRemark(black.getRemark());
			dbEntity.setStatus(BlackStatus.Black.getCode());
			dao().update(t, dbEntity);
			saveBlackHis(t, dbEntity);
		} else {
			dao().insert(t, black);
			saveBlackHis(t, black);
		}
	}

	@Override
	public Black fetchExistBlack(Trace t, Integer type, String blackTarget, Integer status) {
		List<Black> entities = findExistBlacks(t, type, blackTarget, status);
		if (entities != null && !entities.isEmpty()) {
			return entities.get(0);
		}
		return null;
	}

	@Override
	public List<Black> findExistBlacks(Trace t, Integer type, String blackTarget, Integer status) {
		Query q = new Query();
		if (type != null) {
			q.addRule(new Equal("type", type));
		}
		if (!Strings.isBlank(blackTarget)) {
			q.addRule(new Equal("blackTarget", blackTarget));
		}
		if (status != null) {
			q.addRule(new Equal("status", status));
		}

		return dao().find(t, q);
	}

}
