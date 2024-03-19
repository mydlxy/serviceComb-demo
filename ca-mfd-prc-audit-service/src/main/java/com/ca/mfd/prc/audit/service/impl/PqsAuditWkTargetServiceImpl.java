package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsAuditWkTargetEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditWkTargetMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditWkTargetService;
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
 * @Description: Audit质量周目标设置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditWkTargetServiceImpl extends AbstractCrudServiceImpl<IPqsAuditWkTargetMapper, PqsAuditWkTargetEntity> implements IPqsAuditWkTargetService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_WK_TARGET";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsAuditWkTargetEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsAuditWkTargetEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsAuditWkTargetEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsAuditWkTargetEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的Audit质量周目标设置信息
     *
     * @return
     */
    @Override
    public List<PqsAuditWkTargetEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditWkTargetEntity>> getDataFunc = (obj) -> {
            List<PqsAuditWkTargetEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditWkTargetEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}