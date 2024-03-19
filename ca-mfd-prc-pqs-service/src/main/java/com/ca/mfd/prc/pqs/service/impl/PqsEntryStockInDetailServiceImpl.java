package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsEntryStockInDetailEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryStockInDetailMapper;
import com.ca.mfd.prc.pqs.service.IPqsEntryStockInDetailService;
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
public class PqsEntryStockInDetailServiceImpl extends AbstractCrudServiceImpl<IPqsEntryStockInDetailMapper, PqsEntryStockInDetailEntity> implements IPqsEntryStockInDetailService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_STOCK_IN_DETAIL";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryStockInDetailEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryStockInDetailEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryStockInDetailEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryStockInDetailEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsEntryStockInDetailEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryStockInDetailEntity>> getDataFunc = (obj) -> {
            List<PqsEntryStockInDetailEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryStockInDetailEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}