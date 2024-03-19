package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsPlanEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsPlanMapper;
import com.ca.mfd.prc.pqs.service.IPqsPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 检验计划配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsPlanServiceImpl extends AbstractCrudServiceImpl<IPqsPlanMapper, PqsPlanEntity> implements IPqsPlanService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_PLAN";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsPlanEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsPlanEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsPlanEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsPlanEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsPlanEntity> getAllDatas() {
        Function<Object, ? extends List<PqsPlanEntity>> getDataFunc = (obj) -> {
            List<PqsPlanEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsPlanEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}