package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectBlockLogEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsProductDefectBlockLogMapper;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectBlockLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 缺陷堵塞记录服务实现
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
@Service
public class PqsProductDefectBlockLogServiceImpl extends AbstractCrudServiceImpl<IPqsProductDefectBlockLogMapper, PqsProductDefectBlockLogEntity> implements IPqsProductDefectBlockLogService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_PRODUCT_DEFECT_BLOCK_LOG";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsProductDefectBlockLogEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsProductDefectBlockLogEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsProductDefectBlockLogEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsProductDefectBlockLogEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsProductDefectBlockLogEntity> getAllDatas() {
        Function<Object, ? extends List<PqsProductDefectBlockLogEntity>> getDataFunc = (obj) -> {
            List<PqsProductDefectBlockLogEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsProductDefectBlockLogEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}