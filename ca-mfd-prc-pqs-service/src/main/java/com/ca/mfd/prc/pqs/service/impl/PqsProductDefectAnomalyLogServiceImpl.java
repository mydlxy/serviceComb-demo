package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.pqs.dto.ProductDefectAnomalyLogInfo;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectAnomalyLogEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsProductDefectAnomalyLogMapper;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectAnomalyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 产品缺陷日志服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsProductDefectAnomalyLogServiceImpl extends AbstractCrudServiceImpl<IPqsProductDefectAnomalyLogMapper, PqsProductDefectAnomalyLogEntity> implements IPqsProductDefectAnomalyLogService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_PRODUCT_DEFECT_ANOMALY_LOG";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsProductDefectAnomalyLogEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsProductDefectAnomalyLogEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsProductDefectAnomalyLogEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsProductDefectAnomalyLogEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsProductDefectAnomalyLogEntity> getAllDatas() {
        Function<Object, ? extends List<PqsProductDefectAnomalyLogEntity>> getDataFunc = (obj) -> {
            List<PqsProductDefectAnomalyLogEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsProductDefectAnomalyLogEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取激活缺陷日志
     *
     * @param dataId 参数ID
     * @return 日志列表
     */

    @Override
    public List<ProductDefectAnomalyLogInfo> getVehicleDefectAnomalyLog(String dataId) {
        List<ProductDefectAnomalyLogInfo> vehicleDefectAnomalyLogInfos = new ArrayList<>();
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("PRC_PQS_PRODUCT_DEFECT_ANOMALY_ID", dataId, ConditionOper.Equal));
        // ConditionDirection
        List<SortDto> sortInfos = new ArrayList<>();
        sortInfos.add(new SortDto("LAST_UPDATE_DATE", ConditionDirection.DESC));
        List<PqsProductDefectAnomalyLogEntity> list = this.getTopDatas(conditionInfos, sortInfos);
        for (PqsProductDefectAnomalyLogEntity item : list) {
            ProductDefectAnomalyLogInfo info = new ProductDefectAnomalyLogInfo();
            info.setDescription(item.getRemark());
            info.setCreateTime(DateUtils.format(item.getCreationDate(), DateUtils.DATE_TIME_PATTERN));
            vehicleDefectAnomalyLogInfos.add(info);
        }
        return vehicleDefectAnomalyLogInfos;
    }
}