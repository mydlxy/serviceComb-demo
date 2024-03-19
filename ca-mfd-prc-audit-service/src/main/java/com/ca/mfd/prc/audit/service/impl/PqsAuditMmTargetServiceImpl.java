package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.audit.entity.PqsAuditDeptEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditMmTargetEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditMmTargetMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditDeptService;
import com.ca.mfd.prc.audit.service.IPqsAuditMmTargetService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: Audit质量月目标设置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditMmTargetServiceImpl extends AbstractCrudServiceImpl<IPqsAuditMmTargetMapper, PqsAuditMmTargetEntity> implements IPqsAuditMmTargetService {
    @Autowired
    IPqsAuditDeptService pqsAuditDeptService;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_MM_TARGET";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsAuditMmTargetEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsAuditMmTargetEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsAuditMmTargetEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsAuditMmTargetEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsAuditMmTargetEntity entity) {
        if (entity.isDataCheck()) {
            validData(entity);
            QueryWrapper<PqsAuditMmTargetEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsAuditMmTargetEntity::getEvaluationModeCode, entity.getEvaluationModeCode())
                    .eq(PqsAuditMmTargetEntity::getDeptCode, entity.getDeptCode())
                    .eq(PqsAuditMmTargetEntity::getYear, entity.getYear())
                    .eq(PqsAuditMmTargetEntity::getMonth, entity.getMonth());
            PqsAuditMmTargetEntity anomalyEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("评价模式代码" + anomalyEntity.getEvaluationModeCode() + "," + anomalyEntity.getDeptCode() + "," + anomalyEntity.getYear() + "," + anomalyEntity.getMonth() + "," + "已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(PqsAuditMmTargetEntity entity) {
        if (entity.isDataCheck()) {
            validData(entity);
            QueryWrapper<PqsAuditMmTargetEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsAuditMmTargetEntity::getEvaluationModeCode, entity.getEvaluationModeCode())
                    .eq(PqsAuditMmTargetEntity::getDeptCode, entity.getDeptCode())
                    .eq(PqsAuditMmTargetEntity::getYear, entity.getYear()).eq(PqsAuditMmTargetEntity::getMonth, entity.getMonth())
                    .ne(PqsAuditMmTargetEntity::getId, entity.getId());
            PqsAuditMmTargetEntity anomalyEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("评价模式代码" + anomalyEntity.getEvaluationModeCode() + "," + anomalyEntity.getDeptCode() + "," + anomalyEntity.getYear() + "," + anomalyEntity.getMonth() + "," + "已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    private void validData(PqsAuditMmTargetEntity model) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(
                new ConditionDto("DEPT_CODE", model.getDeptCode(), ConditionOper.Equal));
        PqsAuditDeptEntity deptEntity = pqsAuditDeptService.getData(conditionInfos).stream().findFirst().orElse(null);
        if (deptEntity == null) {
            throw new InkelinkException("没有找到责任部门代码数据");
        }
        // 责任部门代码
        model.setDeptCode(deptEntity.getDeptCode());

    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        String keys = "evaluationModeCode,deptCode,year,month";
        validExcelDataUnique(datas, Arrays.asList(keys.split(",")), "Excel中【评价模式代码、责任部门代码、年份、月份】重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("evaluationModeCode"), i + 1, data.get("evaluationModeCode"), "");
            validExcelDataRequire(excelColumnNames.get("evaluationModeName"), i + 1, data.get("evaluationModeName"), "");
            validExcelDataRequire(excelColumnNames.get("deptCode"), i + 1, data.get("deptCode"), "");
            validExcelDataRequire(excelColumnNames.get("deptName"), i + 1, data.get("deptName"), "");
            validExcelDataRequire(excelColumnNames.get("year"), i + 1, data.get("year"), "");
            validExcelDataRequire(excelColumnNames.get("month"), i + 1, data.get("month"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsAuditMmTargetEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsAuditMmTargetEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsAuditMmTargetEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getEvaluationModeCode().equals(a.getEvaluationModeCode())
                    && u.getDeptCode().equals(a.getDeptCode()) && u.getYear().equals(a.getYear()) && u.getMonth().equals(a.getMonth())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsAuditMmTargetEntity> insertList = entities.stream()
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
    public List<PqsAuditMmTargetEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditMmTargetEntity>> getDataFunc = (obj) -> {
            List<PqsAuditMmTargetEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditMmTargetEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}