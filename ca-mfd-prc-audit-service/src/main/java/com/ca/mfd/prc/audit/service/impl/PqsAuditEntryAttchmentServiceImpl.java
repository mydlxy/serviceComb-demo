package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsAuditEntryAttchmentEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditEntryAttchmentMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditEntryAttchmentService;
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
 * @Description: AUDIT附件服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditEntryAttchmentServiceImpl extends AbstractCrudServiceImpl<IPqsAuditEntryAttchmentMapper, PqsAuditEntryAttchmentEntity> implements IPqsAuditEntryAttchmentService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_ENTRY_ATTCHMENT";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsAuditEntryAttchmentEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsAuditEntryAttchmentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsAuditEntryAttchmentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsAuditEntryAttchmentEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsAuditEntryAttchmentEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditEntryAttchmentEntity>> getDataFunc = (obj) -> {
            List<PqsAuditEntryAttchmentEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditEntryAttchmentEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

}