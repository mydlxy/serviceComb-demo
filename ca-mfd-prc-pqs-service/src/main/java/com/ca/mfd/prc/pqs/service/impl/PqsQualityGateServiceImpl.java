package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateBlankEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateTcEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateWorkstationEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityGateMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateBlankService;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateService;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateTcService;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: QG检查项服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsQualityGateServiceImpl extends AbstractCrudServiceImpl<IPqsQualityGateMapper, PqsQualityGateEntity> implements IPqsQualityGateService {

    @Autowired
    private IPqsQualityGateTcService pqsQualityGateTcService;
    @Autowired
    private IPqsQualityGateWorkstationService pqsQualityGateWorkstationService;
    @Autowired
    private IPqsQualityGateBlankService pqsQualityGateBlankService;
    @Autowired
    private IPqsQualityGateAnomalyService pqsQualityGateAnomalyService;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_GATE";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityGateEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityGateEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityGateEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityGateEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsQualityGateEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityGateEntity>> getDataFunc = (obj) -> {
            List<PqsQualityGateEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityGateEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * @param ids 需要删除的数据主键
     */
    @Override
    public void delete(Serializable[] ids) {
        UpdateWrapper<PqsQualityGateTcEntity> qgTc = new UpdateWrapper<>();
        qgTc.lambda().in(PqsQualityGateTcEntity::getPrcPqsQualityGateId, ids);
        pqsQualityGateTcService.delete(qgTc);
        UpdateWrapper<PqsQualityGateWorkstationEntity> qgW = new UpdateWrapper<>();
        qgW.lambda().in(PqsQualityGateWorkstationEntity::getPrcPqsQualityGateId, ids);
        pqsQualityGateWorkstationService.delete(qgW);
        UpdateWrapper<PqsQualityGateBlankEntity> qgB = new UpdateWrapper<>();
        qgB.lambda().in(PqsQualityGateBlankEntity::getPrcPqsQualityGateId, ids);
        pqsQualityGateBlankService.delete(qgB);
        UpdateWrapper<PqsQualityGateAnomalyEntity> qgA = new UpdateWrapper<>();
        qgA.lambda().in(PqsQualityGateAnomalyEntity::getPrcPqsQualityGateBlankId, ids);
        pqsQualityGateAnomalyService.delete(qgA);

        super.delete(ids);
    }
}