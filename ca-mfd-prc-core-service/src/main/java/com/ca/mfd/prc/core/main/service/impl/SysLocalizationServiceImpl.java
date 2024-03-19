package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.core.main.entity.SysLocalizationEntity;
import com.ca.mfd.prc.core.main.mapper.ISysLocalizationMapper;
import com.ca.mfd.prc.core.main.service.ISysLocalizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 国际化
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysLocalizationServiceImpl extends AbstractCrudServiceImpl<ISysLocalizationMapper, SysLocalizationEntity> implements ISysLocalizationService {

    private static final String cacheName = "PRC_SYS_LOCALIZATION";
    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<SysLocalizationEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(SysLocalizationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(SysLocalizationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<SysLocalizationEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(SysLocalizationEntity model) {
        validData(model);
    }

    private void validData(SysLocalizationEntity model) {
        validDataUnique(model.getId(), "CN", model.getCn(), "已经存在值为%s的数据，保存失败", "CN", model.getCn());
        //ValidDataUnique(model.Id, "EN", model.En, "已经存在值为{0}的数据，保存失败", "EN", model.En);
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<SysLocalizationEntity> getAllDatas() {
        List<SysLocalizationEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> columnNames = new HashMap<String, String>(4);
        columnNames.put("Cn", "中文");
        columnNames.put("En", "英文");
        columnNames.put("Lang", "语言");
        return columnNames;
    }

    /**
     * 处理即将导出的数据
     *
     * @param datas
     */
    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("workDay") && data.get("workDay") != null) {
                data.put("workDay", DateUtils.format((Date) data.get("workDay"), "yyyy-MM-dd"));
            }
        }
    }

    /**
     * 导入前验证
     */
    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {

        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "Cn", "");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("Cn"), i + 1, data.get("Cn"), "");
            validExcelDataMaxLength(excelColumnNames.get("Cn"), i + 1, data.get("Cn"), 200, "");
            validExcelDataRequire(excelColumnNames.get("En"), i + 1, data.get("En"), "");
            validExcelDataMaxLength(excelColumnNames.get("En"), i + 1, data.get("En"), 200, "");
        }
    }

    /**
     * 保存导入
     */
    @Override
    public void saveExcelData(List<SysLocalizationEntity> entities) {
        List<String> cns = entities.stream().map(o -> o.getCn()).collect(Collectors.toList());
        //获取更新的数据
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("CN", String.join("|", cns), ConditionOper.In));
        List<SysLocalizationEntity> datas = getData(conditions);

        for (SysLocalizationEntity data : datas) {
            String en = entities.stream().filter(o -> StringUtils.equals(o.getCn(), data.getCn()))
                    .map(o -> o.getEn()).findFirst().orElse(null);
            if (!StringUtils.isBlank(en)) {
                data.setEn(en);
            }
        }
        updateBatchById(datas);
        List<SysLocalizationEntity> addDatas = entities.stream().filter(o -> !datas.stream().anyMatch(p ->
                StringUtils.equals(p.getCn(), o.getCn()))).collect(Collectors.toList());
        insertBatch(addDatas, addDatas.size());
    }
}