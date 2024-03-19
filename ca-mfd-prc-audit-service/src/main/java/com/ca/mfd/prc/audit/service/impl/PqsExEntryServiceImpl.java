package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsExEntryEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExEntryMapper;
import com.ca.mfd.prc.audit.service.IPqsExEntryService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 精致工艺评审单服务实现
 * @date 2024年01月30日
 * @变更说明 BY inkelink At 2024年01月30日
 */
@Service
public class PqsExEntryServiceImpl extends AbstractCrudServiceImpl<IPqsExEntryMapper, PqsExEntryEntity> implements IPqsExEntryService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_ENTRY";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExEntryEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExEntryEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExEntryEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExEntryEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsExEntryEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExEntryEntity>> getDataFunc = (obj) -> {
            List<PqsExEntryEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExEntryEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("manufactureDt") && data.getOrDefault("manufactureDt", null) != null) {
                data.put("manufactureDt", DateUtils.format((Date) data.get("manufactureDt"), DateUtils.DATE_TIME_PATTERN));
            }
            if (data.containsKey("status") && data.getOrDefault("status", null) != null) {
                switch (String.valueOf(data.get("status"))) {
                    case "1":
                        data.put("status", "未开始");
                        break;
                    case "2":
                        data.put("status", "进行中");
                        break;
                    case "90":
                        data.put("status", "完成");
                        break;
                }
            }
        }
    }

}