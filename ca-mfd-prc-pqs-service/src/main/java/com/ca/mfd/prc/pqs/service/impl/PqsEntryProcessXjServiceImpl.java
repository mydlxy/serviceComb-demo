package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessXjEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryProcessXjMapper;
import com.ca.mfd.prc.pqs.service.IPqsEntryProcessXjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 过程检验-巡检明细服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsEntryProcessXjServiceImpl extends AbstractCrudServiceImpl<IPqsEntryProcessXjMapper, PqsEntryProcessXjEntity> implements IPqsEntryProcessXjService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_PROCESS_XJ";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryProcessXjEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryProcessXjEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryProcessXjEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryProcessXjEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsEntryProcessXjEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryProcessXjEntity>> getDataFunc = (obj) -> {
            List<PqsEntryProcessXjEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryProcessXjEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}