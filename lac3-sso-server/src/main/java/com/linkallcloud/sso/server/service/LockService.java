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

		// 重置锁定次数
		l.setStatus(LockStatus.UnLock.getCode());
		l.setReason(LockReson.UnLockByTime.getCode());
		l.setCount(0);
		l.setErr(0);
		l.setLockedTime(new Date());
		l.setOperator("系统");
		l.setRemark("连续锁定次数超过设定数，自动拉黑解锁");
		lockCache(t, l);
		activity().update(t, l);
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
	public void dealAutoLock(Trace t, boolean success, String account, String ip, String remark, LockConfig config) {
		dealAccountAutoLock(t, success, account, remark, config);
		dealIpAutoLock(t, success, ip, remark, config);
	}

	private void dealIpAutoLock(Trace t, boolean success, String ip, String remark, LockConfig config) {
		int errCount = config.getIpErrCount();
		Lock ipLock = fetchExistLock(t, LockBlackType.Ip.getCode(), ip, null);
		if (ipLock != null) {
			if (LockStatus.UnLock.getCode().equals(ipLock.getStatus())) {// unlock status
				if (!success) {
					if (ipLock.getErr() >= errCount - 1) {// 到达累计连续错误次数设定值
						ipLock.setStatus(LockStatus.Lock.getCode());
						ipLock.setLockedTime(new Date());
						ipLock.setCount(ipLock.getCount() + 1);
						ipLock.setErr(0);
						ipLock.setReason(LockReson.LockByLoginFailure.getCode());
						ipLock.setOperator("系统");
						ipLock.setRemark(Strings.isBlank(remark) ? "登录验证失败" : remark);
						lockCache(t, ipLock);
						super.update(t, ipLock);
					} else {
						ipLock.setErr(ipLock.getErr() + 1);
						ipLock.setReason(LockReson.LockByLoginFailure.getCode());
						ipLock.setOperator("系统");
						ipLock.setRemark(Strings.isBlank(remark) ? "登录验证失败" : remark);
						super.update(t, ipLock);
					}
				}
			} else {// lock status
				log.error("############## 警告！警告！警告：有人在Ip锁定状态进行登录尝试，请尽快联系管理员处理。 ##############");
			}
		} else {
			if (!success) {
				Lock entity = new Lock(LockBlackType.Ip.getCode(), ip, LockStatus.UnLock.getCode(), 0, 1,
						LockReson.LockByLoginFailure.getCode(), "系统", Strings.isBlank(remark) ? "登录验证失败" : remark);
				super.insert(t, entity);
			}
		}
	}

	private void dealAccountAutoLock(Trace t, boolean success, String account, String remark, LockConfig config) {
		int errCount = config.getAccountErrCount();
		Lock accountLock = fetchExistLock(t, LockBlackType.Account.getCode(), account, null);
		if (accountLock != null) {
			if (LockStatus.UnLock.getCode().equals(accountLock.getStatus())) {// unlock status
				if (!success) {
					if (accountLock.getErr() >= errCount - 1) {// 到达累计连续错误次数设定值
						accountLock.setStatus(LockStatus.Lock.getCode());
						accountLock.setLockedTime(new Date());
						accountLock.setCount(accountLock.getCount() + 1);
						accountLock.setErr(0);
						accountLock.setReason(LockReson.LockByLoginFailure.getCode());
						accountLock.setOperator("系统");
						accountLock.setRemark(Strings.isBlank(remark) ? "登录验证失败" : remark);
						lockCache(t, accountLock);
						super.update(t, accountLock);
					} else {
						accountLock.setErr(accountLock.getErr() + 1);
						accountLock.setReason(LockReson.LockByLoginFailure.getCode());
						accountLock.setOperator("系统");
						accountLock.setRemark(Strings.isBlank(remark) ? "登录验证失败" : remark);
						super.update(t, accountLock);
					}
				}
			} else {// lock status
				log.error("############## 警告！警告！警告：有人在账号锁定状态进行登录尝试，请尽快联系管理员处理。 ##############");
			}
		} else {
			if (!success) {
				Lock entity = new Lock(LockBlackType.Account.getCode(), account, LockStatus.UnLock.getCode(), 0, 1,
						LockReson.LockByLoginFailure.getCode(), "系统", Strings.isBlank(remark) ? "登录验证失败" : remark);
				super.insert(t, entity);
			}
		}
	}

}
