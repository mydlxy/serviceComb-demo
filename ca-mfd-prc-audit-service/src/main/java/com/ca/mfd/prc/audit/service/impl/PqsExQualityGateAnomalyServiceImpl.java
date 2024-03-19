package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateAnomalyEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExQualityGateAnomalyMapper;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateAnomalyService;
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
 * @Description: 精致工艺 QG检验项-缺陷服务实现
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Service
public class PqsExQualityGateAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsExQualityGateAnomalyMapper, PqsExQualityGateAnomalyEntity> implements IPqsExQualityGateAnomalyService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_QUALITY_GATE_ANOMALY";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExQualityGateAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExQualityGateAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExQualityGateAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExQualityGateAnomalyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsExQualityGateAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExQualityGateAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsExQualityGateAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExQualityGateAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

}