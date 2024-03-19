package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsDeptEntity;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessSmjEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryProcessSmjMapper;
import com.ca.mfd.prc.pqs.service.IPqsEntryProcessSmjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 质检工单-过程检验_首末检验服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsEntryProcessSmjServiceImpl extends AbstractCrudServiceImpl<IPqsEntryProcessSmjMapper, PqsEntryProcessSmjEntity> implements IPqsEntryProcessSmjService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_PROCESS_SMJ";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryProcessSmjEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryProcessSmjEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryProcessSmjEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryProcessSmjEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsEntryProcessSmjEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryProcessSmjEntity>> getDataFunc = (obj) -> {
            List<PqsEntryProcessSmjEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryProcessSmjEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}