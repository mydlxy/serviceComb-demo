package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsTechnologyAlarmRecordEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsTechnologyAlarmRecordMapper;
import com.ca.mfd.prc.pqs.service.IPqsTechnologyAlarmRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @Description: 参数预警记录服务实现
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@Service
public class PqsTechnologyAlarmRecordServiceImpl extends AbstractCrudServiceImpl<IPqsTechnologyAlarmRecordMapper, PqsTechnologyAlarmRecordEntity> implements IPqsTechnologyAlarmRecordService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_TECHNOLOGY_ALARM_RECORD";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsTechnologyAlarmRecordEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsTechnologyAlarmRecordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsTechnologyAlarmRecordEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsTechnologyAlarmRecordEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsTechnologyAlarmRecordEntity> getAllDatas() {
        Function<Object, ? extends List<PqsTechnologyAlarmRecordEntity>> getDataFunc = (obj) -> {
            List<PqsTechnologyAlarmRecordEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsTechnologyAlarmRecordEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}