package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsExEntryAttchmentEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExEntryAttchmentMapper;
import com.ca.mfd.prc.audit.service.IPqsExEntryAttchmentService;
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
 * @Description: 精致工艺附件服务实现
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Service
public class PqsExEntryAttchmentServiceImpl extends AbstractCrudServiceImpl<IPqsExEntryAttchmentMapper, PqsExEntryAttchmentEntity> implements IPqsExEntryAttchmentService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_ENTRY_ATTCHMENT";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExEntryAttchmentEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExEntryAttchmentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExEntryAttchmentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExEntryAttchmentEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsExEntryAttchmentEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExEntryAttchmentEntity>> getDataFunc = (obj) -> {
            List<PqsExEntryAttchmentEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExEntryAttchmentEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}