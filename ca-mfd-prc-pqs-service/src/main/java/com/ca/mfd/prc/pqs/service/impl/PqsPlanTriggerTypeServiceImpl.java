package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsPlanTriggerTypeEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsPlanTriggerTypeMapper;
import com.ca.mfd.prc.pqs.service.IPqsPlanTriggerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 检验计划触发类型配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsPlanTriggerTypeServiceImpl extends AbstractCrudServiceImpl<IPqsPlanTriggerTypeMapper, PqsPlanTriggerTypeEntity> implements IPqsPlanTriggerTypeService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_PLAN_TRIGGER_TYPE";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsPlanTriggerTypeEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsPlanTriggerTypeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsPlanTriggerTypeEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsPlanTriggerTypeEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsPlanTriggerTypeEntity> getAllDatas() {
        Function<Object, ? extends List<PqsPlanTriggerTypeEntity>> getDataFunc = (obj) -> {
            List<PqsPlanTriggerTypeEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsPlanTriggerTypeEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}