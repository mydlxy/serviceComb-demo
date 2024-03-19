package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsAuditGradeEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditGradeMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditGradeService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
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
 * @Description: AUDIT缺陷等级配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditGradeServiceImpl extends AbstractCrudServiceImpl<IPqsAuditGradeMapper, PqsAuditGradeEntity> implements IPqsAuditGradeService {


    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_GRADE";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsAuditGradeEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsAuditGradeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsAuditGradeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsAuditGradeEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsAuditGradeEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("GRADE_CODE", entity.getGradeCode(), ConditionOper.Equal));
            PqsAuditGradeEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getGradeCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeInsert(entity);
    }

    @Override
    public void beforeUpdate(PqsAuditGradeEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("GRADE_CODE", entity.getGradeCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsAuditGradeEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getGradeCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeUpdate(entity);
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "gradeCode", "Excel中项目代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("gradeCode"), i + 1, data.get("gradeCode"), "");
            validExcelDataRequire(excelColumnNames.get("gradeName"), i + 1, data.get("gradeName"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsAuditGradeEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsAuditGradeEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsAuditGradeEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getGradeCode().equals(a.getGradeCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsAuditGradeEntity> insertList = entities.stream()
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

    /**
     * 获取所有的位置信息
     *
     * @return
     */
    @Override
    public List<PqsAuditGradeEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditGradeEntity>> getDataFunc = (obj) -> {
            List<PqsAuditGradeEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditGradeEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public List<ComboInfoDTO> getComboList() {
        return getData(null).stream().map(t -> new ComboInfoDTO(t.getScore().toString(), t.getGradeName())).collect(Collectors.toList());
    }


}