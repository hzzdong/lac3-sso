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
import com.linkallcloud.sso.activity.ILockActivity;
import com.linkallcloud.sso.domain.Lock;
import com.linkallcloud.sso.enums.LockReson;
import com.linkallcloud.sso.enums.LockStatus;
import com.linkallcloud.sso.service.ILockService;

@Service
@Transactional(rollbackFor = Exception.class)
public class LockService extends BaseService<Lock, ILockActivity> implements ILockService {

	@Autowired
	private ILockActivity lockActivity;

	@Override
	public ILockActivity activity() {
		return lockActivity;
	}

	@Override
	public Lock save(Trace t, Lock entity) {
		Lock dbEntity = null;
		if (entity.getId() != null && entity.getId().longValue() > 0) {
			dbEntity = activity().fetchById(t, entity.getId());
		} else {
			dbEntity = fetchExistLock(t, entity);
		}

		if (dbEntity == null) {
			entity.setCount(1);
			return super.save(t, entity);
		} else {
			if (LockStatus.Lock.getCode().equals(entity.getStatus())) {
				if (LockStatus.Lock.getCode().equals(dbEntity.getStatus())) {
					dbEntity.setCount(dbEntity.getCount() + 1);
				} else {
					dbEntity.setCount(1);
				}
			}
			dbEntity.setLockedTime(new Date());
			dbEntity.setOperator(entity.getOperator());
			dbEntity.setReason(entity.getReason());
			dbEntity.setRemark(entity.getRemark());
			dbEntity.setStatus(entity.getStatus());
			return super.save(t, dbEntity);
		}
	}

	@Override
	public Lock fetchExistLock(Trace t, Lock entity) {
		List<Lock> entities = fetchExistLocks(t, entity);
		if (entities != null && !entities.isEmpty()) {
			return entities.get(0);
		}
		return null;
	}

	@Override
	public List<Lock> fetchExistLocks(Trace t, Lock entity) {
		Query q = new Query();
		q.addRule(new Equal("type", entity.getType()));
		q.addRule(new Equal("lockedTarget", entity.getLockedTarget()));
		q.addRule(new Equal("status", LockStatus.Lock.getCode()));
		return activity().find(t, q);
	}

	@Override
	public boolean locks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av) {
		List<Lock> checkedEntities = findByUuidIds(t, uuidIds);
		if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == uuidIds.size()) {
			for (Lock dbEntity : checkedEntities) {
				if (LockStatus.Lock.getCode().equals(dbEntity.getStatus())) {
					dbEntity.setCount(dbEntity.getCount() + 1);
				} else {
					dbEntity.setCount(1);
				}
				dbEntity.setLockedTime(new Date());
				dbEntity.setOperator(av != null ? av.getName() : "");
				dbEntity.setReason(LockReson.LockByHand.getCode());
				dbEntity.setRemark(remark);
				dbEntity.setStatus(LockStatus.Lock.getCode());
				activity().update(t, dbEntity);
			}
			return true;
		}
		log.error("locks 失败，tid：" + t.getTid());
		return false;
	}

	@Override
	public boolean unLocks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av) {
		List<Lock> checkedEntities = findByUuidIds(t, uuidIds);
		if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == uuidIds.size()) {
			for (Lock dbEntity : checkedEntities) {
				dbEntity.setLockedTime(new Date());
				dbEntity.setOperator(av != null ? av.getName() : "");
				dbEntity.setReason(LockReson.UnLockByHand.getCode());
				dbEntity.setRemark(remark);
				dbEntity.setStatus(LockStatus.UnLock.getCode());
				activity().update(t, dbEntity);
			}
			return true;
		}
		log.error("unLocks 失败，tid：" + t.getTid());
		return false;
	}

}
