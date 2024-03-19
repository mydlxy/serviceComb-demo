package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectBlockPolicyEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsProductDefectBlockPolicyMapper;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectBlockPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 缺陷堵塞策略服务实现
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
@Service
public class PqsProductDefectBlockPolicyServiceImpl extends AbstractCrudServiceImpl<IPqsProductDefectBlockPolicyMapper, PqsProductDefectBlockPolicyEntity> implements IPqsProductDefectBlockPolicyService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_PRODUCT_DEFECT_BLOCK_POLICY";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsProductDefectBlockPolicyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsProductDefectBlockPolicyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsProductDefectBlockPolicyEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsProductDefectBlockPolicyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsProductDefectBlockPolicyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsProductDefectBlockPolicyEntity>> getDataFunc = (obj) -> {
            List<PqsProductDefectBlockPolicyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsProductDefectBlockPolicyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}