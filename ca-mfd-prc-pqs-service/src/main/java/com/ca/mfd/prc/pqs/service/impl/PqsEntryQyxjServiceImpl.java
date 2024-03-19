package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessXjEntity;
import com.ca.mfd.prc.pqs.entity.PqsEntryQyxjEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryQyxjMapper;
import com.ca.mfd.prc.pqs.service.IPqsEntryQyxjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 区域巡检服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsEntryQyxjServiceImpl extends AbstractCrudServiceImpl<IPqsEntryQyxjMapper, PqsEntryQyxjEntity> implements IPqsEntryQyxjService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_QYXJ";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryQyxjEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryQyxjEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryQyxjEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryQyxjEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsEntryQyxjEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryQyxjEntity>> getDataFunc = (obj) -> {
            List<PqsEntryQyxjEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryQyxjEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}