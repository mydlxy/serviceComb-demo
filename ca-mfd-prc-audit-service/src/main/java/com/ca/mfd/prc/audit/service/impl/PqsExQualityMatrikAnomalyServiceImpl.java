package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExQualityMatrikAnomalyMapper;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikAnomalyService;
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
 * @Description: 精致工艺百格图-缺陷服务实现
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Service
public class PqsExQualityMatrikAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsExQualityMatrikAnomalyMapper, PqsExQualityMatrikAnomalyEntity> implements IPqsExQualityMatrikAnomalyService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_QUALITY_MATRIK_ANOMALY";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExQualityMatrikAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExQualityMatrikAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExQualityMatrikAnomalyEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExQualityMatrikAnomalyEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有数据
     */
    @Override
    public List<PqsExQualityMatrikAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExQualityMatrikAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsExQualityMatrikAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExQualityMatrikAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}