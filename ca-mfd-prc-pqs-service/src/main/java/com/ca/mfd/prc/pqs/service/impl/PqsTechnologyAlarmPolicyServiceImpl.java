package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsTechnologyAlarmPolicyEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsTechnologyAlarmPolicyMapper;
import com.ca.mfd.prc.pqs.service.IPqsTechnologyAlarmPolicyService;
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
 * @Description: 参数预警配置服务实现
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@Service
public class PqsTechnologyAlarmPolicyServiceImpl extends AbstractCrudServiceImpl<IPqsTechnologyAlarmPolicyMapper, PqsTechnologyAlarmPolicyEntity> implements IPqsTechnologyAlarmPolicyService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_TECHNOLOGY_ALARM_POLICY";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsTechnologyAlarmPolicyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsTechnologyAlarmPolicyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsTechnologyAlarmPolicyEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsTechnologyAlarmPolicyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsTechnologyAlarmPolicyEntity entity) {
        if (entity.isDataCheck()) {
            QueryWrapper<PqsTechnologyAlarmPolicyEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsTechnologyAlarmPolicyEntity::getPolicyNo, entity.getPolicyNo());
            PqsTechnologyAlarmPolicyEntity technologyAlarmPolicyEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (technologyAlarmPolicyEntity != null) {
                throw new InkelinkException("策略编号已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(PqsTechnologyAlarmPolicyEntity entity) {
        if (entity.isDataCheck()) {
            QueryWrapper<PqsTechnologyAlarmPolicyEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsTechnologyAlarmPolicyEntity::getPolicyNo, entity.getPolicyNo());
            queryWrapper.lambda().ne(PqsTechnologyAlarmPolicyEntity::getId, entity.getId());
            PqsTechnologyAlarmPolicyEntity technologyAlarmPolicyEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (technologyAlarmPolicyEntity != null) {
                throw new InkelinkException("策略编号已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    @Override
    public List<PqsTechnologyAlarmPolicyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsTechnologyAlarmPolicyEntity>> getDataFunc = (obj) -> {
            List<PqsTechnologyAlarmPolicyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsTechnologyAlarmPolicyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 数据导入前验证
     *
     * @param datas
     * @param fieldParam
     */
    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        List<String> asList = Arrays.asList("policyNo".split(","));
        validExcelDataUnique(datas, asList, "Excel中策略编号重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("policyNo"), i + 1, data.get("policyNo"), "");
        }
    }

    /**
     * excel数据导入入库
     *
     * @param entities
     */
    @Override
    public void saveExcelData(List<PqsTechnologyAlarmPolicyEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsTechnologyAlarmPolicyEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsTechnologyAlarmPolicyEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getPolicyNo().equals(a.getPolicyNo())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsTechnologyAlarmPolicyEntity> insertList = entities.stream()
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
}