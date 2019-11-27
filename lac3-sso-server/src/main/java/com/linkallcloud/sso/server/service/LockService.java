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
import com.linkallcloud.sso.activity.ILockActivity;
import com.linkallcloud.sso.domain.Black;
import com.linkallcloud.sso.domain.Lock;
import com.linkallcloud.sso.dto.LockConfig;
import com.linkallcloud.sso.enums.BlackReson;
import com.linkallcloud.sso.enums.BlackStatus;
import com.linkallcloud.sso.enums.LockBlackType;
import com.linkallcloud.sso.enums.LockReson;
import com.linkallcloud.sso.enums.LockStatus;
import com.linkallcloud.sso.exception.LockException;
import com.linkallcloud.sso.server.redis.RedisLockBlackCache;
import com.linkallcloud.sso.service.ILockService;

@Service
@Transactional(rollbackFor = Exception.class)
public class LockService extends BaseService<Lock, ILockActivity> implements ILockService {

	@Autowired
	private RedisLockBlackCache lockBlackCache;

	@Autowired
	private ILockActivity lockActivity;

	@Autowired
	private IBlackActivity blackActivity;

	@Override
	public ILockActivity activity() {
		return lockActivity;
	}

	private void lockCache(Trace t, Lock l) {
		if (LockStatus.Lock.getCode().equals(l.getStatus())) {// 加锁
			lockBlackCache.put(RedisLockBlackCache.getLockKey(l.getLockedTarget()), l.getLockedTime());
		} else {// 除黑
			lockBlackCache.remove(RedisLockBlackCache.getLockKey(l.getLockedTarget()));
		}
	}

	private void blackCache(Trace t, Black b) {
		if (BlackStatus.Black.getCode().equals(b.getStatus())) {// 加黑
			lockBlackCache.put(RedisLockBlackCache.getBlackKey(b.getBlackTarget()), b.getBlackTime());
		} else {// 除黑
			lockBlackCache.remove(RedisLockBlackCache.getBlackKey(b.getBlackTarget()));
		}
	}

	@Override
	public Lock save(Trace t, Lock entity) {
		Lock dbEntity = null;
		if (entity.getId() != null && entity.getId().longValue() > 0) {
			dbEntity = activity().fetchById(t, entity.getId());
		} else {
			dbEntity = fetchExistLock(t, entity.getType(), entity.getLockedTarget(), null);
		}

		if (dbEntity == null) {
			entity.setCount(1);
			lockCache(t, entity);
			super.save(t, entity);
			return entity;
		} else {
			if (LockStatus.Lock.getCode().equals(entity.getStatus())) {
				dbEntity.setCount(dbEntity.getCount() + 1);
			}
			dbEntity.setLockedTime(new Date());
			dbEntity.setOperator(entity.getOperator());
			dbEntity.setReason(entity.getReason());
			dbEntity.setRemark(entity.getRemark());
			dbEntity.setStatus(entity.getStatus());
			lockCache(t, dbEntity);
			super.save(t, dbEntity);
			return dbEntity;
		}
	}

	@Override
	public Lock fetchExistLock(Trace t, Integer type, String lockedTarget, Integer status) {
		List<Lock> entities = findExistLocks(t, type, lockedTarget, status);
		if (entities != null && !entities.isEmpty()) {
			return entities.get(0);
		}
		return null;
	}

	@Override
	public List<Lock> findExistLocks(Trace t, Integer type, String lockedTarget, Integer status) {
		Query q = new Query();
		if (type != null) {
			q.addRule(new Equal("type", type));
		}
		if (!Strings.isBlank(lockedTarget)) {
			q.addRule(new Equal("lockedTarget", lockedTarget));
		}
		if (status != null) {
			q.addRule(new Equal("status", status));
		}
		return activity().find(t, q);
	}

	@Override
	public boolean locks(Trace t, Map<String, Long> uuidIds, String remark, AppVisitor av) {
		List<Lock> checkedEntities = findByUuidIds(t, uuidIds);
		if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == uuidIds.size()) {
			for (Lock dbEntity : checkedEntities) {
				dbEntity.setCount(dbEntity.getCount() + 1);
				dbEntity.setLockedTime(new Date());
				dbEntity.setOperator(av != null ? av.getName() : "");
				dbEntity.setReason(LockReson.LockByHand.getCode());
				dbEntity.setRemark(remark);
				dbEntity.setStatus(LockStatus.Lock.getCode());

				lockBlackCache.put(RedisLockBlackCache.getLockKey(dbEntity.getLockedTarget()),
						dbEntity.getLockedTime());
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

				lockBlackCache.remove(RedisLockBlackCache.getLockKey(dbEntity.getLockedTarget()));
				activity().update(t, dbEntity);
			}
			return true;
		}
		log.error("unLocks 失败，tid：" + t.getTid());
		return false;
	}

	@Override
	public void load2Cache(Trace t) {
		List<Lock> all = findExistLocks(t, null, null, LockStatus.Lock.getCode());
		if (all != null && !all.isEmpty()) {
			for (Lock l : all) {
				lockBlackCache.put(RedisLockBlackCache.getLockKey(l.getLockedTarget()), l.getLockedTime());
			}
		}
	}

	@Override
	public void check(Trace t, String lockedTarget) throws LockException {
		if (!Strings.isBlank(lockedTarget)) {
			String key = RedisLockBlackCache.getLockKey(lockedTarget);
			if (lockBlackCache.exists(key)) {
				throw new LockException();
			}
		}
	}

	@Override
	public void autoUnLockBlack(Trace t, LockConfig config) {
		List<Lock> all = findExistLocks(t, null, null, LockStatus.Lock.getCode());
		if (all != null && !all.isEmpty()) {
			for (Lock l : all) {
				if (LockStatus.Lock.getCode().equals(l.getStatus())) {// 锁定
					if (LockBlackType.Account.getCode().equals(l.getType())) {// 账户锁
						if (l.isPeriodOver(config.getAccountLockPeriod())) {
							if (l.getCount() >= config.getAccountLockCount()) {// 自动加黑
								autoBlack(t, l);
							} else {// auto unlock
								autoUnLock(t, l);
							}
						}
					} else {// IP锁
						if (l.isPeriodOver(config.getIpLockPeriod())) {
							if (l.getCount() >= config.getIpLockCount()) {// 自动加黑
								autoBlack(t, l);
							} else {// auto unlock
								autoUnLock(t, l);
							}
						}
					}
				}
			}
		}
	}

	private void autoBlack(Trace t, Lock l) {
		Black b = new Black(l.getType(), l.getLockedTarget(), BlackStatus.Black.getCode(),
				BlackReson.BlackByLock.getCode(), "系统", "锁定次数超过系统设置的连续锁定次数，自动加黑。");
		blackCache(t, b);
		blackActivity.autoBlack(t, b);

		// 解锁，移除锁定表
		removeLock(t, l, LockReson.UnLockByTime.getCode(), "连续锁定次数超过设定数，自动拉黑解锁");
	}

	private void autoUnLock(Trace t, Lock l) {
		l.setStatus(LockStatus.UnLock.getCode());
		l.setReason(LockReson.UnLockByTime.getCode());
		l.setLockedTime(new Date());
		l.setOperator("系统");
		l.setRemark("锁定周期到期自动解锁");
		lockCache(t, l);
		activity().update(t, l);
	}

	@Override
	public void dealAuthAutoLock(Trace t, boolean success, String account, String ip, String remark,
			LockConfig config) {
		dealAuthAutoLockByType(t, success, LockBlackType.Account.getCode(), account, remark, config);
		dealAuthAutoLockByType(t, success, LockBlackType.Ip.getCode(), ip, remark, config);
	}

	private void dealAuthAutoLockByType(Trace t, boolean success, int type, String lockedTarget, String remark,
			LockConfig config) {
		List<Lock> entities = findExistLocks(t, type, lockedTarget, null);
		if (success) {
			if (entities != null && !entities.isEmpty()) {
				for (Lock accountLock : entities) {
					removeLock(t, accountLock, LockReson.UnLockByLoginSuccess.getCode(), remark);
				}
			}
		} else {
			if (entities != null && !entities.isEmpty()) {
				int errCountSetup = LockBlackType.Account.getCode().equals(type) ? config.getAccountErrCount()
						: config.getIpErrCount();
				boolean first = true;
				for (Lock l : entities) {
					if (first) {
						first = false;
						if (l.getErr() >= errCountSetup - 1) {// 到达累计连续错误次数设定值
							lockIncrement(t, l, remark);
						} else {
							lockIncrementErr(t, l, remark);
						}
					} else {
						removeLock(t, l, LockReson.UnLockByLoginSuccess.getCode(), remark);
					}
				}
			} else {
				Lock entity = new Lock(type, lockedTarget, LockStatus.UnLock.getCode(), 0, 1,
						LockReson.LockByLoginFailure.getCode(), "系统", Strings.isBlank(remark) ? "登录验证失败" : remark);
				activity().insert(t, entity);
			}
		}
	}

	/**
	 * 锁定次数加1
	 * 
	 * @param t
	 * @param entity
	 * @param remark
	 */
	private void lockIncrementErr(Trace t, Lock entity, String remark) {
		entity.setErr(entity.getErr() + 1);
		entity.setReason(LockReson.LockByLoginFailure.getCode());
		entity.setOperator("系统");
		entity.setRemark(Strings.isBlank(remark) ? "登录验证失败" : remark);
		activity().update(t, entity);
	}

	/**
	 * 锁定次数加1
	 * 
	 * @param t
	 * @param entity
	 * @param remark
	 */
	private void lockIncrement(Trace t, Lock entity, String remark) {
		entity.setStatus(LockStatus.Lock.getCode());
		entity.setLockedTime(new Date());
		entity.setCount(entity.getCount() + 1);
		entity.setErr(0);
		entity.setReason(LockReson.LockByLoginFailure.getCode());
		entity.setOperator("系统");
		entity.setRemark(Strings.isBlank(remark) ? "登录验证失败" : remark);
		lockCache(t, entity);
		activity().update(t, entity);
	}

	/**
	 * 解锁，移除锁定表中记录
	 * 
	 * @param t
	 * @param entity
	 * @param reson
	 * @param remark
	 */
	private void removeLock(Trace t, Lock entity, int reson, String remark) {
		entity.setStatus(LockStatus.UnLock.getCode());
		entity.setLockedTime(new Date());
		entity.setCount(0);
		entity.setErr(0);
		entity.setReason(reson);
		entity.setOperator("系统");
		entity.setRemark(Strings.isBlank(remark) ? "登录验证成功" : remark);
		lockCache(t, entity);
		activity().remove(t, entity);
	}

}
