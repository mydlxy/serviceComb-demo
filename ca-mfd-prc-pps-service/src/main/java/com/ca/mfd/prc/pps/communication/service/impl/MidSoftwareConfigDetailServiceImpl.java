package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareConfigDetailEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidSoftwareConfigDetailMapper;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareConfigDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @Description: 配置字详情服务实现
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Service
public class MidSoftwareConfigDetailServiceImpl extends AbstractCrudServiceImpl<IMidSoftwareConfigDetailMapper, MidSoftwareConfigDetailEntity> implements IMidSoftwareConfigDetailService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_SOFTWARE_CONFIG_DETAIL";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidSoftwareConfigDetailEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidSoftwareConfigDetailEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidSoftwareConfigDetailEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidSoftwareConfigDetailEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidSoftwareConfigDetailEntity> getAllDatas() {
        Function<Object, ? extends List<MidSoftwareConfigDetailEntity>> getDataFunc = (obj) -> {
            List<MidSoftwareConfigDetailEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<MidSoftwareConfigDetailEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}