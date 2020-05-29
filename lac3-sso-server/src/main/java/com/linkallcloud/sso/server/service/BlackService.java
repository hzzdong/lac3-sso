package com.linkallcloud.sso.server.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.service.BaseService;
import com.linkallcloud.sso.activity.IBlackActivity;
import com.linkallcloud.sso.domain.Black;
import com.linkallcloud.sso.enums.BlackReson;
import com.linkallcloud.sso.enums.BlackStatus;
import com.linkallcloud.sso.exception.BlackListException;
import com.linkallcloud.sso.server.redis.RedisLockBlackCache;
import com.linkallcloud.sso.service.IBlackService;

@Service
@Transactional(rollbackFor = Exception.class)
public class BlackService extends BaseService<Black, IBlackActivity> implements IBlackService {

	@Autowired
	private RedisLockBlackCache lockBlackCache;

	@Autowired
	private IBlackActivity blackActivity;

//	@Autowired
//	private IBlackHisActivity blackHisActivity;

	@Override
	public IBlackActivity activity() {
		return blackActivity;
	}

//	public void saveBlackHis(Trace t, Black entity) {
//		BlackHis his = new BlackHis(entity);
//		blackHisActivity.insert(t, his);
//	}

	private void blackCache(Trace t, Black b) {
		if (BlackStatus.Black.getCode().equals(b.getStatus())) {// 加黑
			lockBlackCache.put(RedisLockBlackCache.getBlackKey(b.getBlackTarget()), b.getBlackTime());
		} else {// 除黑
			lockBlackCache.remove(RedisLockBlackCache.getBlackKey(b.getBlackTarget()));
		}
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
					throw new BlackListException(BlackListException.ERROR_BLACK, "您要添加的对象已经存在黑名单中，无需重复添加。");
				}
			} else {// 除黑
				if (BlackStatus.UnBlack.getCode().equals(dbEntity.getStatus())) {
					throw new BlackListException(BlackListException.ERROR_BLACK, "您要添加的对象已经除黑，无需再进行除黑操作。");
				}
			}

			dbEntity.setBlackTime(new Date());
			dbEntity.setOperator(entity.getOperator());
			dbEntity.setReason(entity.getReason());
			dbEntity.setRemark(entity.getRemark());
			dbEntity.setStatus(entity.getStatus());
			super.save(t, dbEntity);
			blackCache(t, dbEntity);
			// saveBlackHis(t, dbEntity);
			return dbEntity;

		} else {
			super.save(t, entity);
			blackCache(t, entity);
			// saveBlackHis(t, entity);
			return entity;
		}
	}

	@Override
	public Black fetchBlack(Trace t, Integer type, String blackTarget) {
		List<Black> entities = findExistBlacks(t, type, blackTarget);
		if (entities != null && !entities.isEmpty()) {
			return entities.get(0);
		}
		return null;
	}

	@Override
	public Black fetchExistBlack(Trace t, Black entity) {
		List<Black> entities = findExistBlacks(t, entity.getType(), entity.getBlackTarget());
		if (entities != null && !entities.isEmpty()) {
			return entities.get(0);
		}
		return null;
	}

	@Override
	public List<Black> findExistBlacks(Trace t, Integer type, String blackTarget) {
		Query q = new Query();
		if (type != null) {
			q.addRule(new Equal("type", type));
		}
		if (!Strings.isBlank(blackTarget)) {
			q.addRule(new Equal("blackTarget", blackTarget));
		}
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
					dbEntity.setOperator(av != null ? av.name() : "");
					dbEntity.setReason(BlackReson.BlackByHand.getCode());
					dbEntity.setRemark(remark);
					dbEntity.setStatus(BlackStatus.Black.getCode());

					lockBlackCache.put(RedisLockBlackCache.getBlackKey(dbEntity.getBlackTarget()),
							dbEntity.getBlackTime());
					activity().update(t, dbEntity);
					// saveBlackHis(t, dbEntity);
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
					dbEntity.setOperator(av != null ? av.name() : "");
					dbEntity.setReason(BlackReson.UnBlackByHand.getCode());
					dbEntity.setRemark(remark);
					dbEntity.setStatus(BlackStatus.UnBlack.getCode());

					lockBlackCache.remove(RedisLockBlackCache.getBlackKey(dbEntity.getBlackTarget()));
					activity().update(t, dbEntity);
					// saveBlackHis(t, dbEntity);
				}
			}
			return true;
		}
		log.error("unBlacks 失败，tid：" + t.getTid());
		return false;
	}

	@Override
	public void load2Cache(Trace t) {
		List<Black> all = findExistBlacks(t, null, null);
		if (all != null && !all.isEmpty()) {
			for (Black b : all) {
				lockBlackCache.put(RedisLockBlackCache.getBlackKey(b.getBlackTarget()), b.getBlackTime());
			}
		}
	}

	@Override
	public void check(Trace t, String blackTarget) throws BlackListException {
		if (!Strings.isBlank(blackTarget)) {
			String key = RedisLockBlackCache.getBlackKey(blackTarget);
			if (lockBlackCache.exists(key)) {
				throw new BlackListException();
			}
		}

	}

}
