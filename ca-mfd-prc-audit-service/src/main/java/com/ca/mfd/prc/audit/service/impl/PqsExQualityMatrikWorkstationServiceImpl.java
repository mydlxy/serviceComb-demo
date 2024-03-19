package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExQualityMatrikWorkstationMapper;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikWorkstationService;
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
 * @Description: 精致工艺百格图-关联的岗位服务实现
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Service
public class PqsExQualityMatrikWorkstationServiceImpl extends AbstractCrudServiceImpl<IPqsExQualityMatrikWorkstationMapper, PqsExQualityMatrikWorkstationEntity> implements IPqsExQualityMatrikWorkstationService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_QUALITY_MATRIK_WORKSTATION";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExQualityMatrikWorkstationEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExQualityMatrikWorkstationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExQualityMatrikWorkstationEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExQualityMatrikWorkstationEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的百格图关联的岗位
     *
     * @return
     */
    @Override
    public List<PqsExQualityMatrikWorkstationEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExQualityMatrikWorkstationEntity>> getDataFunc = (obj) -> {
            List<PqsExQualityMatrikWorkstationEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExQualityMatrikWorkstationEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

}