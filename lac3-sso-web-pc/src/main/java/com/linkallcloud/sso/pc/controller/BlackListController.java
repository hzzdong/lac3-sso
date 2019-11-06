package com.linkallcloud.sso.pc.controller;


import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.web.controller.BaseLController;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.sso.domain.BlackList;
import com.linkallcloud.sso.excel.BalckListPoiEntity;
import com.linkallcloud.sso.excel.BlackListPoiLog;
import com.linkallcloud.sso.manager.IBlackListManager;
import com.linkallcloud.sso.pc.utils.FileUtil;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/BlackList", method = RequestMethod.POST)
@Module(name = "黑名单")
//@RequirePermissions({"vsms_fee_setup_ds"})
public class BlackListController extends BaseLController<BlackList, IBlackListManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IBlackListManager blackListManager;

    @Override
    protected IBlackListManager manager() {
        return blackListManager;
    }

    /**
     * 导入黑名单-页面
     */
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String importPage(ModelMap modelMap) {
        modelMap.put("code", -1);
        return "page/BlackList/import";
    }

    /**
     * 上传黑名单
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String importFile(HttpServletResponse response, @RequestParam("ulFile") MultipartFile file,
                             ModelMap modelMap, Trace t) throws Exception {
        if (file == null || file.isEmpty()) {
            modelMap.put("code", 10001);
            modelMap.put("errorMessage", "上传文件错误");
            modelMap.put("total", "未知");
            modelMap.put("errorCount", "未知");
        } else {
            List<BalckListPoiEntity> blackEntityList = FileUtil.importExcel(file, 1, 1, BalckListPoiEntity.class);
            if (blackEntityList == null || blackEntityList.isEmpty()) {
                modelMap.put("code", 10002);
                modelMap.put("errorMessage", "文件内容为空");
                modelMap.put("total", 0);
                modelMap.put("errorCount", 0);
            } else {
                List<BlackListPoiLog> errs = blackListManager.insertBatchBlackList(t, blackEntityList);
                if (errs != null && !errs.isEmpty()) {
                    modelMap.put("code", 10003);
                    modelMap.put("total", blackEntityList.size());
                    modelMap.put("errorCount", errs.size());
                    modelMap.put("errors", errs);
                } else {
                    modelMap.put("code", 0);
                    modelMap.put("total", blackEntityList.size());
                }
            }
        }
        return "page/BlackList/import";
    }

    /**
     * 导入黑名单-页面
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(HttpServletResponse response, ModelMap modelMap, Trace t) {
        Query q = new Query();
        q.addRule(new Equal("status", 0));
        List<BlackList> blacks = blackListManager.find(t, q);
        List<BalckListPoiEntity> blackEntityList = new ArrayList<BalckListPoiEntity>();
        if (blacks != null && !blacks.isEmpty()) {
            for (BlackList bl : blacks) {
                String remark = bl.getRemark();
                if (!Strings.isBlank(bl.getRemark()) && bl.getRemark().length() > 255) {
                    remark = bl.getRemark().substring(0, 255);
                }
                BalckListPoiEntity blpe = new BalckListPoiEntity(bl.getMobile(), remark);
                blackEntityList.add(blpe);
            }
        }
        FileUtil.exportExcel(blackEntityList, "黑名单", "黑名单", BalckListPoiEntity.class, "black_list.xls", true, response);
    }

}
