package com.ca.mfd.prc.core.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.core.communication.mapper.IMidApiLogMapper;
import com.ca.mfd.prc.core.communication.service.IMidApiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 *
 * @Description: 接口记录表服务实现
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class MidApiLogServiceImpl extends AbstractCrudServiceImpl<IMidApiLogMapper, MidApiLogEntity> implements IMidApiLogService {
    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_API_LOG";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidApiLogEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidApiLogEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidApiLogEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidApiLogEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidApiLogEntity> getAllDatas() {
        List<MidApiLogEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

}