package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.audit.mapper.IAuditQualityMatrikTcMapper;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikTcEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikTcService;
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
 * @Description: AUDIT百格图-车型服务实现
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@Service
public class AuditQualityMatrikTcServiceImpl extends AbstractCrudServiceImpl<IAuditQualityMatrikTcMapper, AuditQualityMatrikTcEntity> implements IAuditQualityMatrikTcService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_AUDIT_QUALITY_MATRIK_TC";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<AuditQualityMatrikTcEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(AuditQualityMatrikTcEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(AuditQualityMatrikTcEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<AuditQualityMatrikTcEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有数据
     */
    @Override
    public List<AuditQualityMatrikTcEntity> getAllDatas() {
        Function<Object, ? extends List<AuditQualityMatrikTcEntity>> getDataFunc = (obj) -> {
            List<AuditQualityMatrikTcEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<AuditQualityMatrikTcEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}