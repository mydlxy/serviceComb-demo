package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsMmFreezeRecordEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsMmFreezeRecordMapper;
import com.ca.mfd.prc.pqs.service.IPqsMmFreezeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 物料质量冻结记录服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsMmFreezeRecordServiceImpl extends AbstractCrudServiceImpl<IPqsMmFreezeRecordMapper, PqsMmFreezeRecordEntity> implements IPqsMmFreezeRecordService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_MM_FREEZE_RECORD";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsMmFreezeRecordEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsMmFreezeRecordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsMmFreezeRecordEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsMmFreezeRecordEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsMmFreezeRecordEntity> getAllDatas() {
        Function<Object, ? extends List<PqsMmFreezeRecordEntity>> getDataFunc = (obj) -> {
            List<PqsMmFreezeRecordEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsMmFreezeRecordEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}