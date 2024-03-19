package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsAuditDeptEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditDeptMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditDeptService;
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
 * @Description: AUDIT责任部门配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditDeptServiceImpl extends AbstractCrudServiceImpl<IPqsAuditDeptMapper, PqsAuditDeptEntity> implements IPqsAuditDeptService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_DEPT";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsAuditDeptEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsAuditDeptEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsAuditDeptEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsAuditDeptEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsAuditDeptEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEPT_CODE", entity.getDeptCode(), ConditionOper.Equal));
            PqsAuditDeptEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getDeptCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeInsert(entity);
    }

    @Override
    public void beforeUpdate(PqsAuditDeptEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEPT_CODE", entity.getDeptCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsAuditDeptEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getDeptCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeUpdate(entity);
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "deptCode", "Excel中项目代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("deptCode"), i + 1, data.get("deptCode"), "");
            validExcelDataRequire(excelColumnNames.get("deptName"), i + 1, data.get("deptName"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsAuditDeptEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsAuditDeptEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsAuditDeptEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getDeptCode().equals(a.getDeptCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsAuditDeptEntity> insertList = entities.stream()
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
    public List<PqsAuditDeptEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditDeptEntity>> getDataFunc = (obj) -> {
            List<PqsAuditDeptEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditDeptEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}