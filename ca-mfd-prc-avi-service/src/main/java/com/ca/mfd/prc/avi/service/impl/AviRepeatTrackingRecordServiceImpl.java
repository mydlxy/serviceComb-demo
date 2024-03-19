package com.ca.mfd.prc.avi.service.impl;

import com.ca.mfd.prc.avi.entity.AviRepeatTrackingRecordEntity;
import com.ca.mfd.prc.avi.mapper.IAviRepeatTrackingRecordMapper;
import com.ca.mfd.prc.avi.service.IAviRepeatTrackingRecordService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 关键过点配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviRepeatTrackingRecordServiceImpl extends AbstractCrudServiceImpl<IAviRepeatTrackingRecordMapper, AviRepeatTrackingRecordEntity> implements IAviRepeatTrackingRecordService {

    private static final Logger logger = LoggerFactory.getLogger(AviRepeatTrackingRecordServiceImpl.class);
    private static final String cacheName = "PRC_AVI_REPEAT_TRACKING_RECORD";
    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;

    @Override

    public void beforeInsert(AviRepeatTrackingRecordEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(AviRepeatTrackingRecordEntity model) {
        valid(model);
    }

    private void valid(AviRepeatTrackingRecordEntity model) {
        validDataUnique(model.getId(), "AVI_CODE", model.getAviCode(), "已经存在代码为" + model.getAviCode() + "的数据", "", "");
    }


    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(AviRepeatTrackingRecordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(AviRepeatTrackingRecordEntity model) {
        removeCache();
    }

    private void removeCache() {
        localCache.removeObject(cacheName);
    }


    @Override
    public List<AviRepeatTrackingRecordEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<AviRepeatTrackingRecordEntity>> getDataFunc = (obj) -> {
                List<AviRepeatTrackingRecordEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<AviRepeatTrackingRecordEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if ("true".equals(data.get("isEnable"))) {
                data.put("isEnable", "是");
            } else {
                data.put("isEnable", "否");
            }
        }
    }
}