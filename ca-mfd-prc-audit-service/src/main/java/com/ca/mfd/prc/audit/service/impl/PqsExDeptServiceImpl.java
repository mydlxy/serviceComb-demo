package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsExDeptEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExDeptMapper;
import com.ca.mfd.prc.audit.service.IPqsExDeptService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 精致工艺责任部门配置服务实现
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
@Service
public class PqsExDeptServiceImpl extends AbstractCrudServiceImpl<IPqsExDeptMapper, PqsExDeptEntity> implements IPqsExDeptService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_DEPT";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExDeptEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExDeptEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExDeptEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExDeptEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsExDeptEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEPT_CODE", entity.getDeptCode(), ConditionOper.Equal));
            PqsExDeptEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getDeptCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeInsert(entity);
    }

    @Override
    public void beforeUpdate(PqsExDeptEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEPT_CODE", entity.getDeptCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsExDeptEntity deptEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity != null) {
                throw new InkelinkException("编码" + deptEntity.getDeptCode() + "已存在,请不要重复添加");
            }
        }
        super.beforeUpdate(entity);
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "deptCode", "Excel中部门代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("deptCode"), i + 1, data.get("deptCode"), "");
            validExcelDataRequire(excelColumnNames.get("deptName"), i + 1, data.get("deptName"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsExDeptEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsExDeptEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsExDeptEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getDeptCode().equals(a.getDeptCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsExDeptEntity> insertList = entities.stream()
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
    public List<PqsExDeptEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExDeptEntity>> getDataFunc = (obj) -> {
            List<PqsExDeptEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExDeptEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public List<ComboDataDTO> getComboList() {
        List<ComboDataDTO> collect = getData(null).stream()
                .sorted(Comparator.comparing(PqsExDeptEntity::getDisplayNo)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setText(c.getDeptName());
                    dto.setValue(c.getDeptCode());
                    return dto;
                }).collect(Collectors.toList());
        return  collect;
    }
}