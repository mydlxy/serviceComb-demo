package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsFunctionEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsFunctionMapper;
import com.ca.mfd.prc.pqs.service.IPqsFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 质检功能配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsFunctionServiceImpl extends AbstractCrudServiceImpl<IPqsFunctionMapper, PqsFunctionEntity> implements IPqsFunctionService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_FUNCTION";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsFunctionEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsFunctionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsFunctionEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsFunctionEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsFunctionEntity> getAllDatas() {
        Function<Object, ? extends List<PqsFunctionEntity>> getDataFunc = (obj) -> {
            List<PqsFunctionEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsFunctionEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}