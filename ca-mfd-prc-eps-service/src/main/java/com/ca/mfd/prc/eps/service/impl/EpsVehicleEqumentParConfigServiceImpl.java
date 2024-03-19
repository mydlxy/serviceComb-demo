package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsVehicleEqumentParConfigMapper;
import com.ca.mfd.prc.eps.service.IEpsVehicleEqumentParConfigService;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentParConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 追溯设备工艺参数
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsVehicleEqumentParConfigServiceImpl extends AbstractCrudServiceImpl<IEpsVehicleEqumentParConfigMapper, EpsVehicleEqumentParConfigEntity> implements IEpsVehicleEqumentParConfigService {
    private static final Logger logger = LoggerFactory.getLogger(EpsVehicleEqumentParConfigServiceImpl.class);

    private static final String CACHE_NAME = "PRC_EPS_VEHICLE_EQUMENT_PAR_CONFIG";
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<EpsVehicleEqumentParConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsVehicleEqumentParConfigEntity model) {
        validDataUnique(model.getId(), "EQUMENT_PAR_CODE", model.getEqumentParCode(), "已经存在编码为%s的数据", "PRC_EPS_VEHICLE_EQUMENT_CONFIG_ID", String.valueOf(model.getPrcEpsVehicleEqumentConfigId()));
        removeCache();
    }

    @Override
    public void afterUpdate(EpsVehicleEqumentParConfigEntity model) {
        validDataUnique(model.getId(), "EQUMENT_PAR_CODE", model.getEqumentParCode(), "已经存在编码为%s的数据", "PRC_EPS_VEHICLE_EQUMENT_CONFIG_ID", String.valueOf(model.getPrcEpsVehicleEqumentConfigId()));
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsVehicleEqumentParConfigEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<EpsVehicleEqumentParConfigEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsVehicleEqumentParConfigEntity>> getDataFunc = (obj) -> {
                List<EpsVehicleEqumentParConfigEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsVehicleEqumentParConfigEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, 60 * 10);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }
}