package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsEntryStockInEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryStockInMapper;
import com.ca.mfd.prc.pqs.service.IPqsEntryStockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 入库质检工单服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsEntryStockInServiceImpl extends AbstractCrudServiceImpl<IPqsEntryStockInMapper, PqsEntryStockInEntity> implements IPqsEntryStockInService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_STOCK_IN";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryStockInEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryStockInEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryStockInEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryStockInEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsEntryStockInEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryStockInEntity>> getDataFunc = (obj) -> {
            List<PqsEntryStockInEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryStockInEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}