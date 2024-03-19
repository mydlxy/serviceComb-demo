package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsAuditStageEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditStageMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditStageService;
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
 * @Description: AUDIT阶段配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditStageServiceImpl extends AbstractCrudServiceImpl<IPqsAuditStageMapper, PqsAuditStageEntity> implements IPqsAuditStageService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_STAGE";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsAuditStageEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsAuditStageEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsAuditStageEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsAuditStageEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsAuditStageEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("STAGE_CODE", entity.getStageCode(), ConditionOper.Equal));
            PqsAuditStageEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getStageCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeInsert(entity);
    }

    @Override
    public void beforeUpdate(PqsAuditStageEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("STAGE_CODE", entity.getStageCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsAuditStageEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getStageCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeUpdate(entity);
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "stageCode", "Excel中项目代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("stageCode"), i + 1, data.get("stageCode"), "");
            validExcelDataRequire(excelColumnNames.get("stageName"), i + 1, data.get("stageName"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsAuditStageEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsAuditStageEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsAuditStageEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getStageCode().equals(a.getStageCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsAuditStageEntity> insertList = entities.stream()
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
    public List<PqsAuditStageEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditStageEntity>> getDataFunc = (obj) -> {
            List<PqsAuditStageEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditStageEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}