package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsPartsScrapEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsPartsScrapMapper;
import com.ca.mfd.prc.pqs.service.IPqsPartsScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 质检工单-评审工单服务实现
 * @date 2023年09月17日
 * @变更说明 BY inkelink At 2023年09月17日
 */
@Service
public class PqsPartsScrapServiceImpl extends AbstractCrudServiceImpl<IPqsPartsScrapMapper, PqsPartsScrapEntity> implements IPqsPartsScrapService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_PARTS_SCRAP";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsPartsScrapEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsPartsScrapEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsPartsScrapEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsPartsScrapEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsPartsScrapEntity> getAllDatas() {
        Function<Object, ? extends List<PqsPartsScrapEntity>> getDataFunc = (obj) -> {
            List<PqsPartsScrapEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsPartsScrapEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}