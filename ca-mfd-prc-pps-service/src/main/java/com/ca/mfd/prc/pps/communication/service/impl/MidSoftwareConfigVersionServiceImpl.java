package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareConfigVersionEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidSoftwareConfigVersionMapper;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareConfigVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @Description: 配置字版本服务实现
 * @author inkelink
 * @date 2023年11月27日
 * @变更说明 BY inkelink At 2023年11月27日
 */
@Service
public class MidSoftwareConfigVersionServiceImpl extends AbstractCrudServiceImpl<IMidSoftwareConfigVersionMapper, MidSoftwareConfigVersionEntity> implements IMidSoftwareConfigVersionService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_SOFTWARE_CONFIG_VERSION";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidSoftwareConfigVersionEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidSoftwareConfigVersionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidSoftwareConfigVersionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidSoftwareConfigVersionEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidSoftwareConfigVersionEntity> getAllDatas() {
        Function<Object, ? extends List<MidSoftwareConfigVersionEntity>> getDataFunc = (obj) -> {
            List<MidSoftwareConfigVersionEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<MidSoftwareConfigVersionEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}