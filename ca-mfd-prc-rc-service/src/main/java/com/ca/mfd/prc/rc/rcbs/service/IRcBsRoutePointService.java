package com.ca.mfd.prc.rc.rcbs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePointEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcBsRoutePointService extends ICrudService<RcBsRoutePointEntity> {

    /**
     * 根据 路由区ID 获取路由点列表
     *
     * @param areaId 路由区ID
     * @return 路由点列表
     */
    List<RcBsRoutePointEntity> getEntityByAreaId(Long areaId);
}