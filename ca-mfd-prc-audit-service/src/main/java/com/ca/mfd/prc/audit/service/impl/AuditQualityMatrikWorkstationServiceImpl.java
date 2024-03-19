package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.audit.mapper.IAuditQualityMatrikWorkstationMapper;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikWorkstationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @Description: AUDIT百格图关联的岗位服务实现
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@Service
public class AuditQualityMatrikWorkstationServiceImpl extends AbstractCrudServiceImpl<IAuditQualityMatrikWorkstationMapper, AuditQualityMatrikWorkstationEntity> implements IAuditQualityMatrikWorkstationService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_AUDIT_QUALITY_MATRIK_WORKSTATION";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<AuditQualityMatrikWorkstationEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(AuditQualityMatrikWorkstationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(AuditQualityMatrikWorkstationEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<AuditQualityMatrikWorkstationEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的百格图关联的岗位
     *
     * @return
     */
    @Override
    public List<AuditQualityMatrikWorkstationEntity> getAllDatas() {
        Function<Object, ? extends List<AuditQualityMatrikWorkstationEntity>> getDataFunc = (obj) -> {
            List<AuditQualityMatrikWorkstationEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<AuditQualityMatrikWorkstationEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

}