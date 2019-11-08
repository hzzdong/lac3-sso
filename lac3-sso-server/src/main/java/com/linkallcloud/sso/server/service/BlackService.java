package com.linkallcloud.sso.server.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.IBlackActivity;
import com.linkallcloud.sso.activity.IBlackHisActivity;
import com.linkallcloud.sso.domain.Black;
import com.linkallcloud.sso.domain.BlackHis;
import com.linkallcloud.sso.enums.BlackReson;
import com.linkallcloud.sso.enums.BlackStatus;
import com.linkallcloud.sso.exception.BlackListException;
import com.linkallcloud.sso.service.IBlackService;

@Service
@Transactional(rollbackFor = Exception.class)
public class BlackService extends BaseService<Black, IBlackActivity> implements IBlackService {

	@Autowired
	private IBlackActivity blackActivity;

	@Autowired
	private IBlackHisActivity blackHisActivity;

	@Override
	public IBlackActivity activity() {
		return blackActivity;
	}

	public void saveBlackHis(Trace t, Black entity) {
		BlackHis his = new BlackHis(entity);
		blackHisActivity.insert(t, his);
	}

	@Override
	public Black save(Trace t, Black entity) {
		Black dbEntity = null;
		if (entity.getId() != null && entity.getId().longValue() > 0) {
			dbEntity = activity().fetchById(t, entity.getId());
		} else {
			dbEntity = fetchExistBlack(t, entity);
		}

		if (dbEntity != null) {
			if (BlackStatus.Black.getCode().equals(entity.getStatus())) {// 加黑
				if (BlackStatus.Black.getCode().equals(dbEntity.getStatus())) {
					throw new BlackListException(BlackListException.ARG_CODE_BLACKLIST, "您要添加的对象已经存在黑名单中，无需重复添加。");
				}
			} else {// 除黑
				if (BlackStatus.UnBlack.getCode().equals(dbEntity.getStatus())) {
					throw new BlackListException(BlackListException.ARG_CODE_BLACKLIST, "您要添加的对象已经除黑，无需再进行除黑操作。");
				}
			}

			dbEntity.setBlackTime(new Date());
			dbEntity.setOperator(entity.getOperator());
			dbEntity.setReason(entity.getReason());
			dbEntity.setRemark(entity.getRemark());
			dbEntity.setStatus(entity.getStatus());
			super.save(t, dbEntity);
			saveBlackHis(t, dbEntity);
			return dbEntity;

		} else {
			super.save(t, entity);
			saveBlackHis(t, entity);
			return entity;
		}
	}

	@Override
	public Black fetchExistBlack(Trace t, Black entity) {
		List<Black> entities = fetchExistBlacks(t, entity);
		if (entities != null && !entities.isEmpty()) {
			return entities.get(0);
		}
		return null;
	}

	@Override
	public List<Black> fetchExistBlacks(Trace t, Black entity) {
		Query q = new Query();
		q.addRule(new Equal("type", entity.getType()));
		q.addRule(new Equal("blackTarget", entity.getBlackTarget()));
		q.addRule(new Equal("status", BlackStatus.Black.getCode()));
		return activity().find(t, q);
	}

	@Override
	public boolean blacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av) {
		List<Black> checkedEntities = findByUuidIds(t, uuidIds);
		if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == uuidIds.size()) {
			for (Black dbEntity : checkedEntities) {
				if (!BlackStatus.Black.getCode().equals(dbEntity.getStatus())) {
					dbEntity.setBlackTime(new Date());
					dbEntity.setOperator(av != null ? av.getName() : "");
					dbEntity.setReason(BlackReson.BlackByHand.getCode());
					dbEntity.setRemark(remark);
					dbEntity.setStatus(BlackStatus.Black.getCode());
					activity().update(t, dbEntity);

					saveBlackHis(t, dbEntity);
				}
			}
			return true;
		}
		log.error("blacks 失败，tid：" + t.getTid());
		return false;
	}

	@Override
	public boolean unBlacks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av) {
		List<Black> checkedEntities = findByUuidIds(t, uuidIds);
		if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == uuidIds.size()) {
			for (Black dbEntity : checkedEntities) {
				if (BlackStatus.Black.getCode().equals(dbEntity.getStatus())) {
					dbEntity.setBlackTime(new Date());
					dbEntity.setOperator(av != null ? av.getName() : "");
					dbEntity.setReason(BlackReson.UnBlackByHand.getCode());
					dbEntity.setRemark(remark);
					dbEntity.setStatus(BlackStatus.UnBlack.getCode());
					activity().update(t, dbEntity);

					saveBlackHis(t, dbEntity);
				}
			}
			return true;
		}
		log.error("unBlacks 失败，tid：" + t.getTid());
		return false;
	}

}
