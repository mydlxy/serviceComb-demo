package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.audit.entity.PqsExEntryDefectAnomalyEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExEntryDefectAnomalyMapper;
import com.ca.mfd.prc.audit.service.IPqsExEntryDefectAnomalyService;
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
 * @Description: 精致工艺缺陷记录服务实现
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Service
public class PqsExEntryDefectAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsExEntryDefectAnomalyMapper, PqsExEntryDefectAnomalyEntity> implements IPqsExEntryDefectAnomalyService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_ENTRY_DEFECT_ANOMALY";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsExEntryDefectAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExEntryDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExEntryDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExEntryDefectAnomalyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsExEntryDefectAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExEntryDefectAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsExEntryDefectAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExEntryDefectAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {

        datas.forEach(data -> {
            // 导出处理时间格式化
            if (data.containsKey("activateTime") && data.getOrDefault("activateTime", null) != null) {
                data.put("activateTime", DateUtils.format((Date) data.get("activateTime"), DateUtils.DATE_TIME_PATTERN));
            }
            if (data.containsKey("repairTime") && data.getOrDefault("repairTime", null) != null) {
                data.put("repairTime", DateUtils.format((Date) data.get("repairTime"), DateUtils.DATE_TIME_PATTERN));
            }
            if (data.containsKey("recheckTime") && data.getOrDefault("recheckTime", null) != null) {
                data.put("recheckTime", DateUtils.format((Date) data.get("recheckTime"), DateUtils.DATE_TIME_PATTERN));
            }

            // 状态处理
            if (data.containsKey("status") && data.getOrDefault("status", null) != null) {
                switch (String.valueOf(data.get("status"))) {
                    case "1":
                        data.put("status", "已激活");
                        break;
                    case "2":
                        data.put("status", "已修复");
                        break;
                    case "3":
                        data.put("status", "未发现");
                        break;
                    case "4":
                        data.put("status", "合格");
                        break;
                    case "5":
                        data.put("status", "不合格");
                        break;
                    default:
                        break;
                }
            }
        });
    }

}