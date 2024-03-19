package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsAuditProjectEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditProjectMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditProjectService;
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
 * @Description: AUDIT项目配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditProjectServiceImpl extends AbstractCrudServiceImpl<IPqsAuditProjectMapper, PqsAuditProjectEntity> implements IPqsAuditProjectService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_PROJECT";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsAuditProjectEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsAuditProjectEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsAuditProjectEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsAuditProjectEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsAuditProjectEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("PROJECT_CODE", entity.getProjectCode(), ConditionOper.Equal));
            PqsAuditProjectEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getProjectCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeInsert(entity);
    }

    @Override
    public void beforeUpdate(PqsAuditProjectEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("PROJECT_CODE", entity.getProjectCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsAuditProjectEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getProjectCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeUpdate(entity);
    }

    /**
     * 获取所有的位置信息
     *
     * @return
     */
    @Override
    public List<PqsAuditProjectEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditProjectEntity>> getDataFunc = (obj) -> {
            List<PqsAuditProjectEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditProjectEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "projectCode", "Excel中项目代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("projectCode"), i + 1, data.get("projectCode"), "");
            validExcelDataRequire(excelColumnNames.get("projectName"), i + 1, data.get("projectName"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsAuditProjectEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsAuditProjectEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsAuditProjectEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getProjectCode().equals(a.getProjectCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsAuditProjectEntity> insertList = entities.stream()
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