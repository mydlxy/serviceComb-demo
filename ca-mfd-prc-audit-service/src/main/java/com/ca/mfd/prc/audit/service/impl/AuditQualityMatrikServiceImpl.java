package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikTcEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikAnomalyService;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikTcService;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikWorkstationService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.audit.mapper.IAuditQualityMatrikMapper;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @Description: AUDIT百格图服务实现
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@Service
public class AuditQualityMatrikServiceImpl extends AbstractCrudServiceImpl<IAuditQualityMatrikMapper, AuditQualityMatrikEntity> implements IAuditQualityMatrikService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_AUDIT_QUALITY_MATRIK";
    private void removeCache() {
        localCache.removeObject(cacheName);
    }


    @Autowired
    private IAuditQualityMatrikTcService auditQualityMatrikTcService;
    @Autowired
    private IAuditQualityMatrikAnomalyService auditQualityMatrikAnomalyService;
    @Autowired
    private IAuditQualityMatrikWorkstationService auditQualityMatrikWorkstationService;
    @Resource
    private IAuditQualityMatrikMapper auditQualityMatrikMapper;
    @Override
    public void afterDelete(Wrapper<AuditQualityMatrikEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(AuditQualityMatrikEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(AuditQualityMatrikEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<AuditQualityMatrikEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void delete(Serializable[] ids) {
        UpdateWrapper<AuditQualityMatrikWorkstationEntity> delWrap = new UpdateWrapper<>();
        delWrap.lambda().in(AuditQualityMatrikWorkstationEntity::getPrcAuditQualityMatrikId, ids);
        auditQualityMatrikWorkstationService.delete(delWrap);
        UpdateWrapper<AuditQualityMatrikAnomalyEntity> anomalyWrap = new UpdateWrapper<>();
        anomalyWrap.lambda().in(AuditQualityMatrikAnomalyEntity::getPrcAuditQualityMatrikId, ids);
        auditQualityMatrikAnomalyService.delete(anomalyWrap);
        UpdateWrapper<AuditQualityMatrikTcEntity> tcWrap = new UpdateWrapper<>();
        tcWrap.lambda().in(AuditQualityMatrikTcEntity::getPrcAuditQualityMatrikId, ids);
        auditQualityMatrikTcService.delete(tcWrap);
        super.delete(ids);
    }

    /**
     * 获取所有数据
     */
    @Override
    public List<AuditQualityMatrikEntity> getAllDatas() {
        Function<Object, ? extends List<AuditQualityMatrikEntity>> getDataFunc = (obj) -> {
            List<AuditQualityMatrikEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<AuditQualityMatrikEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public AuditQualityMatrikEntity get(Serializable id) {
        AuditQualityMatrikEntity data = auditQualityMatrikMapper.selectById(id);
        data.setVehicles(auditQualityMatrikTcService.getAllDatas().stream()
                .filter(t -> StringUtils.equals(t.getPrcAuditQualityMatrikId().toString(), id.toString()))
                .collect(Collectors.toList()));
        data.setDefects(auditQualityMatrikAnomalyService.getAllDatas().stream()
                .filter(t -> StringUtils.equals(t.getPrcAuditQualityMatrikId().toString(), id.toString()))
                .collect(Collectors.toList()));
        return data;
    }

}