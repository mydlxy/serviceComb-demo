package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectAnomalyLogEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsMmDefectAnomalyLogMapper;
import com.ca.mfd.prc.pqs.service.IPqsMmDefectAnomalyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @Description: 零部件缺陷活动日志服务实现
 * @author inkelink
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@Service
public class PqsMmDefectAnomalyLogServiceImpl extends AbstractCrudServiceImpl<IPqsMmDefectAnomalyLogMapper, PqsMmDefectAnomalyLogEntity> implements IPqsMmDefectAnomalyLogService {

    @Autowired
    private LocalCache localCache;

    private final String cacheName = "PRC_PQS_MM_DEFECT_ANOMALY_LOG";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsMmDefectAnomalyLogEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsMmDefectAnomalyLogEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsMmDefectAnomalyLogEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsMmDefectAnomalyLogEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsMmDefectAnomalyLogEntity> getAllDatas() {
        Function<Object, ? extends List<PqsMmDefectAnomalyLogEntity>> getDataFunc = (obj) -> {
            List<PqsMmDefectAnomalyLogEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsMmDefectAnomalyLogEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}