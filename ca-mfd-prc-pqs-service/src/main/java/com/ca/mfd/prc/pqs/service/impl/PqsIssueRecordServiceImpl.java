package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsIssueRecordEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsIssueRecordMapper;
import com.ca.mfd.prc.pqs.service.IPqsIssueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @Description: 问题预警记录服务实现
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@Service
public class PqsIssueRecordServiceImpl extends AbstractCrudServiceImpl<IPqsIssueRecordMapper, PqsIssueRecordEntity> implements IPqsIssueRecordService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ISSUE_RECORD";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsIssueRecordEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsIssueRecordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsIssueRecordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsIssueRecordEntity> updateWrapper) {
        removeCache();
    }

    /**
     * @return
     */
    @Override
    public List<PqsIssueRecordEntity> getAllDatas() {
        Function<Object, ? extends List<PqsIssueRecordEntity>> getDataFunc = (obj) -> {
            List<PqsIssueRecordEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsIssueRecordEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}