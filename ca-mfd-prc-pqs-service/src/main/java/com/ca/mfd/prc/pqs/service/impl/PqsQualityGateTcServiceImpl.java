package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateTcEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityGateTcMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateTcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: QG检查项-车型服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsQualityGateTcServiceImpl extends AbstractCrudServiceImpl<IPqsQualityGateTcMapper, PqsQualityGateTcEntity> implements IPqsQualityGateTcService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_GATE_TC";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityGateTcEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityGateTcEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityGateTcEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityGateTcEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsQualityGateTcEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityGateTcEntity>> getDataFunc = (obj) -> {
            List<PqsQualityGateTcEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityGateTcEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}