package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsMmScrapRecordEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsMmScrapRecordMapper;
import com.ca.mfd.prc.pqs.service.IPqsMmScrapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @Description: 压铸废料管理服务实现
 * @author inkelink
 * @date 2023年10月27日
 * @变更说明 BY inkelink At 2023年10月27日
 */
@Service
public class PqsMmScrapRecordServiceImpl extends AbstractCrudServiceImpl<IPqsMmScrapRecordMapper, PqsMmScrapRecordEntity> implements IPqsMmScrapRecordService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_MM_SCRAP_RECORD";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsMmScrapRecordEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsMmScrapRecordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsMmScrapRecordEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsMmScrapRecordEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有压铸废料管理记录
     *
     * @return
     */
    @Override
    public List<PqsMmScrapRecordEntity> getAllDatas() {
        Function<Object, ? extends List<PqsMmScrapRecordEntity>> getDataFunc = (obj) -> {
            List<PqsMmScrapRecordEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsMmScrapRecordEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}