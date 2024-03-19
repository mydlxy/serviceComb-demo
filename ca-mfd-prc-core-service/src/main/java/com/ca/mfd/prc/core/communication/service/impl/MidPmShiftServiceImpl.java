package com.ca.mfd.prc.core.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.communication.mapper.IMidPmShiftMapper;
import com.ca.mfd.prc.core.communication.entity.MidPmShiftEntity;
import com.ca.mfd.prc.core.communication.service.IMidPmShiftService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 班次中间表服务实现
 * @author inkelink
 * @date 2023年10月16日
 * @变更说明 BY inkelink At 2023年10月16日
 */
@Service
public class MidPmShiftServiceImpl extends AbstractCrudServiceImpl<IMidPmShiftMapper, MidPmShiftEntity> implements IMidPmShiftService {


    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_PM_COUNTRY";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidPmShiftEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidPmShiftEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidPmShiftEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidPmShiftEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidPmShiftEntity> getAllDatas() {
        List<MidPmShiftEntity> datas = localCache.getObject(cacheName);
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