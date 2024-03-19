package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsGradeEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsGradeMapper;
import com.ca.mfd.prc.pqs.service.IPqsGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 缺陷等级配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsGradeServiceImpl extends AbstractCrudServiceImpl<IPqsGradeMapper, PqsGradeEntity> implements IPqsGradeService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_GRADE";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsGradeEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsGradeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsGradeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsGradeEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsGradeEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("GRADE_CODE", entity.getGradeCode(), ConditionOper.Equal));
            PqsGradeEntity gradeEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (gradeEntity != null) {
                throw new InkelinkException("编码" + gradeEntity.getGradeCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeInsert(entity);
    }

    @Override
    public void beforeUpdate(PqsGradeEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("GRADE_CODE", entity.getGradeCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsGradeEntity gradeEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (gradeEntity != null) {
                throw new InkelinkException("编码" + gradeEntity.getGradeCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeUpdate(entity);
    }

    /**
     * 获取缺陷等级配置数据
     *
     * @return
     */
    @Override
    public List<PqsGradeEntity> getAllDatas() {
        Function<Object, ? extends List<PqsGradeEntity>> getDataFunc = (obj) -> {
            List<PqsGradeEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsGradeEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 导入前验证
     *
     * @param datas
     * @param fieldParam
     */
    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "gradeCode", "Excel中等级编码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("gradeCode"), i + 1, data.get("gradeCode"), "");
            validExcelDataRequire(excelColumnNames.get("gradeName"), i + 1, data.get("gradeName"), "");
        }
    }

    /**
     * excel导入保存数据
     *
     * @param entities
     */
    @Override
    public void saveExcelData(List<PqsGradeEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsGradeEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsGradeEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getGradeCode().equals(a.getGradeCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsGradeEntity> insertList = entities.stream()
                .filter(e -> !updateList.contains(e)).collect(Collectors.toList());

        if (insertList.size() > 0) {
            super.insertBatch(insertList);
        }
        if (updateList.size() > 0) {
            super.updateBatchById(updateList);
        }
        removeCache();
        saveChange();
    }
}