package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.audit.mapper.IAuditQualityMatrikAnomalyMapper;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikAnomalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @Description: AUDIT百格图-缺陷服务实现
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@Service
public class AuditQualityMatrikAnomalyServiceImpl extends AbstractCrudServiceImpl<IAuditQualityMatrikAnomalyMapper, AuditQualityMatrikAnomalyEntity> implements IAuditQualityMatrikAnomalyService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_AUDIT_QUALITY_MATRIK_ANOMALY";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<AuditQualityMatrikAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(AuditQualityMatrikAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(AuditQualityMatrikAnomalyEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<AuditQualityMatrikAnomalyEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有数据
     */
    @Override
    public List<AuditQualityMatrikAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<AuditQualityMatrikAnomalyEntity>> getDataFunc = (obj) -> {
            List<AuditQualityMatrikAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<AuditQualityMatrikAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}