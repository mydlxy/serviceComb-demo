package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQualityRouteRecordEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityRouteRecordMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityRouteRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 车辆去向指定记录服务实现
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
@Service
public class PqsQualityRouteRecordServiceImpl extends AbstractCrudServiceImpl<IPqsQualityRouteRecordMapper, PqsQualityRouteRecordEntity> implements IPqsQualityRouteRecordService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_ROUTE_RECORD";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityRouteRecordEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityRouteRecordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityRouteRecordEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityRouteRecordEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsQualityRouteRecordEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityRouteRecordEntity>> getDataFunc = (obj) -> {
            List<PqsQualityRouteRecordEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityRouteRecordEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}