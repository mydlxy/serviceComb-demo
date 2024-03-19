package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsProductQgCheckListRecordEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsProductQgCheckListRecordMapper;
import com.ca.mfd.prc.pqs.service.IPqsProductQgCheckListRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 产品-QG必检项服务实现
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@Service
public class PqsProductQgCheckListRecordServiceImpl extends AbstractCrudServiceImpl<IPqsProductQgCheckListRecordMapper, PqsProductQgCheckListRecordEntity> implements IPqsProductQgCheckListRecordService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_PRODUCT_QG_CHECK_LIST_RECORD";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsProductQgCheckListRecordEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsProductQgCheckListRecordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsProductQgCheckListRecordEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsProductQgCheckListRecordEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsProductQgCheckListRecordEntity> getAllDatas() {
        Function<Object, ? extends List<PqsProductQgCheckListRecordEntity>> getDataFunc = (obj) -> {
            List<PqsProductQgCheckListRecordEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsProductQgCheckListRecordEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

}