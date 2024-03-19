package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikTcEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityMatrikTcMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikTcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 百格图-车型服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsQualityMatrikTcServiceImpl extends AbstractCrudServiceImpl<IPqsQualityMatrikTcMapper, PqsQualityMatrikTcEntity> implements IPqsQualityMatrikTcService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_MATRIK_TC";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityMatrikTcEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityMatrikTcEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityMatrikTcEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityMatrikTcEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有数据
     */
    @Override
    public List<PqsQualityMatrikTcEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityMatrikTcEntity>> getDataFunc = (obj) -> {
            List<PqsQualityMatrikTcEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityMatrikTcEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    private List<PqsQualityMatrikTcEntity> getDatas() {
        QueryWrapper<PqsQualityMatrikTcEntity> qry = new QueryWrapper<>();
        qry.lambda().orderByAsc(PqsQualityMatrikTcEntity::getModel);
        return selectList(qry);
    }
}