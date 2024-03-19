package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectAnomalyEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditDeptEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditGradeEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditDefectAnomalyMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditDefectAnomalyService;
import com.ca.mfd.prc.audit.service.IPqsAuditDeptService;
import com.ca.mfd.prc.audit.service.IPqsAuditGradeService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
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
 * @Description: 组合缺陷库服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditDefectAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsAuditDefectAnomalyMapper, PqsAuditDefectAnomalyEntity> implements IPqsAuditDefectAnomalyService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_DEFECT_ANOMALY";
    @Autowired
    private IPqsAuditGradeService pqsAuditGradeService;

    @Autowired
    private IPqsAuditDeptService pqsAuditDeptService;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsAuditDefectAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsAuditDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsAuditDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsAuditDefectAnomalyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsAuditDefectAnomalyEntity entity) {
        if (entity.isDataCheck()) {
            validData(entity);
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_ANOMALY_CODE", entity.getDefectAnomalyCode(), ConditionOper.Equal));
            PqsAuditDefectAnomalyEntity anomalyEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("编码" + anomalyEntity.getDefectAnomalyCode() + "已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(PqsAuditDefectAnomalyEntity entity) {
        if (entity.isDataCheck()) {
            validData(entity);
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_ANOMALY_CODE", entity.getDefectAnomalyCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsAuditDefectAnomalyEntity anomalyEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("编码" + anomalyEntity.getDefectAnomalyCode() + "已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    private void validData(PqsAuditDefectAnomalyEntity model) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(
                new ConditionDto("GRADE_CODE", model.getGradeCode(), ConditionOper.Equal));
        PqsAuditGradeEntity gradeEntity = pqsAuditGradeService.getData(conditionInfos).stream().findFirst().orElse(null);
        if (gradeEntity == null) {
            throw new InkelinkException("没有找到缺陷等级代码数据");
        }
        conditionInfos.clear();
        conditionInfos.add(
                new ConditionDto("DEPT_CODE", model.getResponsibleDeptCode(), ConditionOper.Equal));
        PqsAuditDeptEntity deptEntity = pqsAuditDeptService.getData(conditionInfos).stream().findFirst().orElse(null);
        if (deptEntity == null) {
            throw new InkelinkException("没有找到责任部门代码数据");
        }
        // 缺陷等级代码
        model.setGradeName(gradeEntity.getGradeName() + "[" + gradeEntity.getScore() + "]");
        // 责任部门代码
        model.setResponsibleDeptName(deptEntity.getDeptName());

    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "defectAnomalyCode", "Excel中组合代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("defectAnomalyCode"), i + 1, data.get("defectAnomalyCode"), "");
            validExcelDataRequire(excelColumnNames.get("defectAnomalyDescription"), i + 1, data.get("defectAnomalyDescription"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsAuditDefectAnomalyEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsAuditDefectAnomalyEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsAuditDefectAnomalyEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getDefectAnomalyCode().equals(a.getDefectAnomalyCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsAuditDefectAnomalyEntity> insertList = entities.stream()
                .filter(e -> !updateList.contains(e)).collect(Collectors.toList());

        if (insertList.size() > 0) {
            insertBatch(insertList, insertList.size());
        }
        if (updateList.size() > 0) {
            updateBatchById(updateList, updateList.size());
        }
        removeCache();
        saveChange();
    }

    @Override
    public List<PqsAuditDefectAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditDefectAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsAuditDefectAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditDefectAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}