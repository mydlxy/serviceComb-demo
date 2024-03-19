package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikTcEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExQualityMatrikTcMapper;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikTcService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 精致工艺百格图-车型服务实现
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Service
public class PqsExQualityMatrikTcServiceImpl extends AbstractCrudServiceImpl<IPqsExQualityMatrikTcMapper, PqsExQualityMatrikTcEntity> implements IPqsExQualityMatrikTcService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_QUALITY_MATRIK_TC";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExQualityMatrikTcEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExQualityMatrikTcEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExQualityMatrikTcEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExQualityMatrikTcEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有数据
     */
    @Override
    public List<PqsExQualityMatrikTcEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExQualityMatrikTcEntity>> getDataFunc = (obj) -> {
            List<PqsExQualityMatrikTcEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExQualityMatrikTcEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}