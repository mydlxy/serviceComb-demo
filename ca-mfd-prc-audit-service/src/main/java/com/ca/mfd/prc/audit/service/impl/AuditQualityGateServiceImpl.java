package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.audit.entity.AuditQualityGateAnomalyEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityGateBlankEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityGateEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityGateTcEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityGateWorkstationEntity;
import com.ca.mfd.prc.audit.mapper.IAuditQualityGateMapper;
import com.ca.mfd.prc.audit.service.IAuditQualityGateAnomalyService;
import com.ca.mfd.prc.audit.service.IAuditQualityGateBlankService;
import com.ca.mfd.prc.audit.service.IAuditQualityGateService;
import com.ca.mfd.prc.audit.service.IAuditQualityGateTcService;
import com.ca.mfd.prc.audit.service.IAuditQualityGateWorkstationService;
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
 *
 * @Description: AUDIT QG检查项服务实现
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@Service
public class AuditQualityGateServiceImpl extends AbstractCrudServiceImpl<IAuditQualityGateMapper, AuditQualityGateEntity> implements IAuditQualityGateService {

    @Autowired
    private IAuditQualityGateTcService auditQualityGateTcService;
    @Autowired
    private IAuditQualityGateWorkstationService auditQualityGateWorkstationService;
    @Autowired
    private IAuditQualityGateBlankService auditQualityGateBlankService;
    @Autowired
    private IAuditQualityGateAnomalyService auditQualityGateAnomalyService;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_AUDIT_QUALITY_GATE";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<AuditQualityGateEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(AuditQualityGateEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(AuditQualityGateEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<AuditQualityGateEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<AuditQualityGateEntity> getAllDatas() {
        Function<Object, ? extends List<AuditQualityGateEntity>> getDataFunc = (obj) -> {
            List<AuditQualityGateEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<AuditQualityGateEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
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
        UpdateWrapper<AuditQualityGateTcEntity> qgTc = new UpdateWrapper<>();
        qgTc.lambda().in(AuditQualityGateTcEntity::getPrcAuditQualityGateId, ids);
        auditQualityGateTcService.delete(qgTc);
        UpdateWrapper<AuditQualityGateWorkstationEntity> qgW = new UpdateWrapper<>();
        qgW.lambda().in(AuditQualityGateWorkstationEntity::getPrcAuditQualityGateId, ids);
        auditQualityGateWorkstationService.delete(qgW);
        UpdateWrapper<AuditQualityGateBlankEntity> qgB = new UpdateWrapper<>();
        qgB.lambda().in(AuditQualityGateBlankEntity::getPrcAuditQualityGateId, ids);
        auditQualityGateBlankService.delete(qgB);
        UpdateWrapper<AuditQualityGateAnomalyEntity> qgA = new UpdateWrapper<>();
        qgA.lambda().in(AuditQualityGateAnomalyEntity::getPrcAuditQualityGateBlankId, ids);
        auditQualityGateAnomalyService.delete(qgA);

        super.delete(ids);
    }
}