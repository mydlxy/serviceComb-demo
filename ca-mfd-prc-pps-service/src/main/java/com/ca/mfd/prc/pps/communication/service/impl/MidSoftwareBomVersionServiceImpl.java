package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomVersionEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidSoftwareBomVersionMapper;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @Description: 单车软件清单版本服务实现
 * @author inkelink
 * @date 2023年11月23日
 * @变更说明 BY inkelink At 2023年11月23日
 */
@Service
public class MidSoftwareBomVersionServiceImpl extends AbstractCrudServiceImpl<IMidSoftwareBomVersionMapper, MidSoftwareBomVersionEntity> implements IMidSoftwareBomVersionService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_SOFTWARE_BOM_VERSION";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidSoftwareBomVersionEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidSoftwareBomVersionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidSoftwareBomVersionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidSoftwareBomVersionEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidSoftwareBomVersionEntity> getAllDatas() {
        Function<Object, ? extends List<MidSoftwareBomVersionEntity>> getDataFunc = (obj) -> {
            List<MidSoftwareBomVersionEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<MidSoftwareBomVersionEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}