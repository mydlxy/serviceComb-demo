package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsProductMmLockPolicyEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsProductMmLockPolicyMapper;
import com.ca.mfd.prc.pqs.service.IPqsProductMmLockPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 追溯件阻塞服务实现
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
@Service
public class PqsProductMmLockPolicyServiceImpl extends AbstractCrudServiceImpl<IPqsProductMmLockPolicyMapper, PqsProductMmLockPolicyEntity> implements IPqsProductMmLockPolicyService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_PRODUCT_MM_LOCK_POLICY";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsProductMmLockPolicyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsProductMmLockPolicyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsProductMmLockPolicyEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsProductMmLockPolicyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsProductMmLockPolicyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsProductMmLockPolicyEntity>> getDataFunc = (obj) -> {
            List<PqsProductMmLockPolicyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsProductMmLockPolicyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}