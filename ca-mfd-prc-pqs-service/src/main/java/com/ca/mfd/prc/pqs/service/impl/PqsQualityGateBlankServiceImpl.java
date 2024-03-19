package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateBlankEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityGateBlankMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateBlankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: QG检验项-色块服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsQualityGateBlankServiceImpl extends AbstractCrudServiceImpl<IPqsQualityGateBlankMapper, PqsQualityGateBlankEntity> implements IPqsQualityGateBlankService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_GATE_BLANK";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityGateBlankEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityGateBlankEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityGateBlankEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityGateBlankEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsQualityGateBlankEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityGateBlankEntity>> getDataFunc = (obj) -> {
            List<PqsQualityGateBlankEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityGateBlankEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}