package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateWorkstationEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityGateWorkstationMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: QG检查项关联的岗位服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsQualityGateWorkstationServiceImpl extends AbstractCrudServiceImpl<IPqsQualityGateWorkstationMapper, PqsQualityGateWorkstationEntity> implements IPqsQualityGateWorkstationService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_GATE_WORKSTATION";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityGateWorkstationEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityGateWorkstationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityGateWorkstationEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityGateWorkstationEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsQualityGateWorkstationEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityGateWorkstationEntity>> getDataFunc = (obj) -> {
            List<PqsQualityGateWorkstationEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityGateWorkstationEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}