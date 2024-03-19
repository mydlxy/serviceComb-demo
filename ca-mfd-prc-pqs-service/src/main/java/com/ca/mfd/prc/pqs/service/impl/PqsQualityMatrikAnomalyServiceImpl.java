package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityMatrikAnomalyMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikAnomalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 百格图-缺陷服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsQualityMatrikAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsQualityMatrikAnomalyMapper, PqsQualityMatrikAnomalyEntity> implements IPqsQualityMatrikAnomalyService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_MATRIK_ANOMALY";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityMatrikAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityMatrikAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityMatrikAnomalyEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityMatrikAnomalyEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有数据
     */
    @Override
    public List<PqsQualityMatrikAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityMatrikAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsQualityMatrikAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityMatrikAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    private List<PqsQualityMatrikAnomalyEntity> getDatas() {
        QueryWrapper<PqsQualityMatrikAnomalyEntity> qry = new QueryWrapper<>();
        qry.lambda().orderByAsc(PqsQualityMatrikAnomalyEntity::getDefectAnomalyCode);
        return selectList(qry);
    }

}