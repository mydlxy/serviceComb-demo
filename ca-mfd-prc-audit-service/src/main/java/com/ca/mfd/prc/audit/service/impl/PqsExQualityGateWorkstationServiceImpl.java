package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateWorkstationEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExQualityGateWorkstationMapper;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateWorkstationService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 精致工艺 QG检查项关联的岗位服务实现
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Service
public class PqsExQualityGateWorkstationServiceImpl extends AbstractCrudServiceImpl<IPqsExQualityGateWorkstationMapper, PqsExQualityGateWorkstationEntity> implements IPqsExQualityGateWorkstationService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_QUALITY_GATE_WORKSTATION";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExQualityGateWorkstationEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExQualityGateWorkstationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExQualityGateWorkstationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExQualityGateWorkstationEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsExQualityGateWorkstationEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExQualityGateWorkstationEntity>> getDataFunc = (obj) -> {
            List<PqsExQualityGateWorkstationEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExQualityGateWorkstationEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}