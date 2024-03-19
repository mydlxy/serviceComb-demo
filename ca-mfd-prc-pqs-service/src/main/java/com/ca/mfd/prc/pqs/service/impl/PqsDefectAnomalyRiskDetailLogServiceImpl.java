package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskDetailLogEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsDefectAnomalyRiskDetailLogMapper;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyRiskDetailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 质量围堵-操作日志服务实现
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@Service
public class PqsDefectAnomalyRiskDetailLogServiceImpl extends AbstractCrudServiceImpl<IPqsDefectAnomalyRiskDetailLogMapper, PqsDefectAnomalyRiskDetailLogEntity> implements IPqsDefectAnomalyRiskDetailLogService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_DEFECT_ANOMALY_RISK_DETAIL_LOG";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsDefectAnomalyRiskDetailLogEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsDefectAnomalyRiskDetailLogEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsDefectAnomalyRiskDetailLogEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsDefectAnomalyRiskDetailLogEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsDefectAnomalyRiskDetailLogEntity> getAllDatas() {
        Function<Object, ? extends List<PqsDefectAnomalyRiskDetailLogEntity>> getDataFunc = (obj) -> {
            List<PqsDefectAnomalyRiskDetailLogEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsDefectAnomalyRiskDetailLogEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}