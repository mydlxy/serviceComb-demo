package com.ca.mfd.prc.rc.rcavi.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteHoldEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由区暂存表服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcAviRouteHoldService extends ICrudService<RcAviRouteHoldEntity> {
    /**
     * 暂存车辆-后台
     *
     * @param areaId   路由ID
     * @param tpsCodes 车辆识别码
     * @param reason   原因
     */
    void addHoldList(String areaId, List<String> tpsCodes, String reason);

    /**
     * 解除暂存车辆-后台
     *
     * @param ids    主键集合
     * @param reason 原因
     */
    void removeHoldList(String[] ids, String reason);
}