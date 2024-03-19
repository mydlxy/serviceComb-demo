package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateAnomalyEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateBlankEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateTcEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateWorkstationEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExQualityGateMapper;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateAnomalyService;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateBlankService;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateService;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateTcService;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateWorkstationService;
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
 * @Description: 精致工艺 QG检查项服务实现
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Service
public class PqsExQualityGateServiceImpl extends AbstractCrudServiceImpl<IPqsExQualityGateMapper, PqsExQualityGateEntity> implements IPqsExQualityGateService {
    @Autowired
    private IPqsExQualityGateTcService pqsExQualityGateTcService;
    @Autowired
    private IPqsExQualityGateWorkstationService pqsExQualityGateWorkstationService;
    @Autowired
    private IPqsExQualityGateBlankService pqsExQualityGateBlankService;
    @Autowired
    private IPqsExQualityGateAnomalyService pqsExQualityGateAnomalyService;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_QUALITY_GATE";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExQualityGateEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExQualityGateEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExQualityGateEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExQualityGateEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsExQualityGateEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExQualityGateEntity>> getDataFunc = (obj) -> {
            List<PqsExQualityGateEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExQualityGateEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
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
        UpdateWrapper<PqsExQualityGateTcEntity> qgTc = new UpdateWrapper<>();
        qgTc.lambda().in(PqsExQualityGateTcEntity::getPrcPqsExQualityGateId, ids);
        pqsExQualityGateTcService.delete(qgTc);
        UpdateWrapper<PqsExQualityGateWorkstationEntity> qgW = new UpdateWrapper<>();
        qgW.lambda().in(PqsExQualityGateWorkstationEntity::getPrcPqsExQualityGateId, ids);
        pqsExQualityGateWorkstationService.delete(qgW);
        UpdateWrapper<PqsExQualityGateBlankEntity> qgB = new UpdateWrapper<>();
        qgB.lambda().in(PqsExQualityGateBlankEntity::getPrcPqsExQualityGateId, ids);
        pqsExQualityGateBlankService.delete(qgB);
        UpdateWrapper<PqsExQualityGateAnomalyEntity> qgA = new UpdateWrapper<>();
        qgA.lambda().in(PqsExQualityGateAnomalyEntity::getPrcPqsExQualityGateBlankId, ids);
        pqsExQualityGateAnomalyService.delete(qgA);

        super.delete(ids);
    }

}