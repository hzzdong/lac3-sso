package com.linkallcloud.sso.server.activity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linkallcloud.core.activity.BaseActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.sso.activity.IBlackListActivity;
import com.linkallcloud.sso.domain.BlackList;
import com.linkallcloud.sso.excel.BalckListPoiEntity;
import com.linkallcloud.sso.excel.BlackListPoiLog;
import com.linkallcloud.sso.exception.BlackListException;
import com.linkallcloud.sso.server.dao.IBlackListDao;

@Component
public class BlackListActivity extends BaseActivity<BlackList, IBlackListDao> implements IBlackListActivity {

	@Autowired
	private IBlackListDao blackListDao;

	@Override
	public IBlackListDao dao() {
		return blackListDao;
	}

	public BlackListActivity() {
		super();
	}

	@Override
    public List<BlackListPoiLog> insertBatchBlackList(Trace t, List<BalckListPoiEntity> blackEntityList) {
        if (blackEntityList == null || blackEntityList.isEmpty()) {
            return null;
        }

        List<BlackListPoiLog> result = new ArrayList<BlackListPoiLog>();
        int rowNum = 3;
        for (int i = 0; i < blackEntityList.size(); i++) {
            BalckListPoiEntity hu = blackEntityList.get(i);
            BlackListPoiLog log = checkBlackListEntity(rowNum, hu, blackEntityList);// 必填项检查
            if (log != null) {
                result.add(log);
            }
            rowNum++;
        }

        if (!result.isEmpty()) {
            return result;
        }

        rowNum = 3;
        List<BlackList> blacks = new ArrayList<BlackList>();
        for (int i = 0; i < blackEntityList.size(); i++) {
            BalckListPoiEntity hu = blackEntityList.get(i);
            try {
                checkBlackList(t, rowNum, hu);
                blacks.add(assembleBlackList(rowNum, hu));
                batchInsertBlackList(t, blacks, false);
            } catch (BaseRuntimeException e) {
                result.add(new BlackListPoiLog(rowNum, hu.getMobile(), e.getMessage()));
            }
            rowNum++;
        }
        batchInsertBlackList(t, blacks, true);

        return result;
    }

    private void batchInsertBlackList(Trace t, List<BlackList> blacks, boolean last) {
        if (blacks != null && !blacks.isEmpty()) {
            if (last) {
                dao().batchInsertBlackList(t, blacks);
            } else if (blacks.size() >= 100) {
                dao().batchInsertBlackList(t, blacks);
                blacks = new ArrayList<BlackList>();
            }
        }
    }

    private BlackList assembleBlackList(int rowNum, BalckListPoiEntity hu) {
        return new BlackList(hu.getMobile(), hu.getRemark());
    }

    private void checkBlackList(Trace t, int rowNum, BalckListPoiEntity hu) {
        BlackList dbDomain = dao().fetchByMobile(t, hu.getMobile());
        if (dbDomain != null) {
            throw new BlackListException("MOBILE_EXIST", String.format("手机(%s)已存在", hu.getMobile()));
        }
    }

    private BlackListPoiLog checkBlackListEntity(int rowNum, BalckListPoiEntity ble, List<BalckListPoiEntity> blackEntityList) {
        String error = "";
        if (Strings.isBlank(ble.getMobile())) {
            error += "手机不能为空;";
        }

        if (Strings.isBlank(error)) {
            if (blackEntityList != null && !blackEntityList.isEmpty()) {
                int count = 0;
                for (BalckListPoiEntity b : blackEntityList) {
                    if (ble.getMobile().equals(b.getMobile())) {
                        count++;
                    }
                }
                if (count > 1) {
                    error += "手机重复，请检查导入模板中数据;";
                }
            }
        }

        if (!Strings.isBlank(error)) {
            return new BlackListPoiLog(rowNum, ble.getMobile(), error);
        }

        return null;
    }

}
