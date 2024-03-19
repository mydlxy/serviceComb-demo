package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.AuditQualityGateBlankEntity;
import com.ca.mfd.prc.audit.mapper.IAuditQualityGateBlankMapper;
import com.ca.mfd.prc.audit.service.IAuditQualityGateBlankService;
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
 * @Description: AUDIT QG检验项-色块服务实现
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@Service
public class AuditQualityGateBlankServiceImpl extends AbstractCrudServiceImpl<IAuditQualityGateBlankMapper, AuditQualityGateBlankEntity> implements IAuditQualityGateBlankService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_AUDIT_QUALITY_GATE_BLANK";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<AuditQualityGateBlankEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(AuditQualityGateBlankEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(AuditQualityGateBlankEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<AuditQualityGateBlankEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<AuditQualityGateBlankEntity> getAllDatas() {
        Function<Object, ? extends List<AuditQualityGateBlankEntity>> getDataFunc = (obj) -> {
            List<AuditQualityGateBlankEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<AuditQualityGateBlankEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}