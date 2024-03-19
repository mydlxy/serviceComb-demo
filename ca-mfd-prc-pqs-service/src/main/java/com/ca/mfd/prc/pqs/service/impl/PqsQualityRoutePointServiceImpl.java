package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQualityRoutePointEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityRoutePointMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityRoutePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 车辆去向点位配置表服务实现
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
@Service
public class PqsQualityRoutePointServiceImpl extends AbstractCrudServiceImpl<IPqsQualityRoutePointMapper, PqsQualityRoutePointEntity> implements IPqsQualityRoutePointService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_ROUTE_POINT";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityRoutePointEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityRoutePointEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityRoutePointEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityRoutePointEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsQualityRoutePointEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(PqsQualityRoutePointEntity model) {
        valid(model);
    }

    @Override
    public List<PqsQualityRoutePointEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityRoutePointEntity>> getDataFunc = (obj) -> {
            List<PqsQualityRoutePointEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityRoutePointEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }


    private void valid(PqsQualityRoutePointEntity model) {
        Long recordNumber = getRecordCount(model);
        if (recordNumber > 0) {
            throw new InkelinkException("该工位已存在调度代码,请勿重复添加");
        }
    }

    private Long getRecordCount(PqsQualityRoutePointEntity model) {
        QueryWrapper<PqsQualityRoutePointEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsQualityRoutePointEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsQualityRoutePointEntity::getWorkstationCode, model.getWorkstationCode());
        lambdaQueryWrapper.eq(PqsQualityRoutePointEntity::getAreaCode, model.getAreaCode());
        lambdaQueryWrapper.ne(PqsQualityRoutePointEntity::getId, model.getId());
        return selectCount(queryWrapper);
    }

}