package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsEntryAuditDetailEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryAuditDetailMapper;
import com.ca.mfd.prc.pqs.service.IPqsEntryAuditDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 评审工单明细服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsEntryAuditDetailServiceImpl extends AbstractCrudServiceImpl<IPqsEntryAuditDetailMapper, PqsEntryAuditDetailEntity> implements IPqsEntryAuditDetailService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_AUDIT_DETAIL";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryAuditDetailEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryAuditDetailEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryAuditDetailEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryAuditDetailEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取质检附件数据
     *
     * @return
     */
    @Override
    public List<PqsEntryAuditDetailEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryAuditDetailEntity>> getDataFunc = (obj) -> {
            List<PqsEntryAuditDetailEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryAuditDetailEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}