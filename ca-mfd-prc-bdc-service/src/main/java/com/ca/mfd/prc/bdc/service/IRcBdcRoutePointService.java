package com.ca.mfd.prc.bdc.service;

import com.ca.mfd.prc.bdc.entity.RcBdcRoutePointEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点服务
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
public interface IRcBdcRoutePointService extends ICrudService<RcBdcRoutePointEntity> {
    /**
     * 根据 路由区ID 获取路由点列表
     *
     * @param areaId 路由区ID
     * @return 路由点列表
     */
    List<RcBdcRoutePointEntity> getEntityByAreaId(Long areaId);
}