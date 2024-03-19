package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.dto.GetAnomalyByQualityGateBlankIdInfo;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateAnomalyEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityGateAnomalyMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateAnomalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: QG检验项-缺陷服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsQualityGateAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsQualityGateAnomalyMapper, PqsQualityGateAnomalyEntity> implements IPqsQualityGateAnomalyService {

    @Autowired
    IPqsQualityGateAnomalyMapper pqsQualityGateAnomalyMapper;

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_GATE_ANOMALY";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityGateAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityGateAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityGateAnomalyEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityGateAnomalyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsQualityGateAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityGateAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsQualityGateAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityGateAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 节点ID
     * @param tpsCode            tps编码
     * @return 返回缺陷列表
     */
    @Override
    public List<GetAnomalyByQualityGateBlankIdInfo> getAnomalyByQualityGateBlankId(String qualityGateBlankId, String tpsCode) {
        return pqsQualityGateAnomalyMapper.getAnomalyByQualityGateBlankId(qualityGateBlankId, tpsCode);
    }
}