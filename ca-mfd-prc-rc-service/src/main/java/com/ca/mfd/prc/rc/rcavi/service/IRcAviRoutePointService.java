package com.ca.mfd.prc.rc.rcavi.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRoutePointEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcAviRoutePointService extends ICrudService<RcAviRoutePointEntity> {

    /**
     * 根据 路由区ID 获取路由点列表
     *
     * @param areaId 路由区ID
     * @return 路由点列表
     */
    List<RcAviRoutePointEntity> getEntityByAreaId(Long areaId);
}