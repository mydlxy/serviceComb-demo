package com.ca.mfd.prc.bdc.service;

import com.ca.mfd.prc.bdc.entity.RcBdcRouteHoldEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由区暂存表服务
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
public interface IRcBdcRouteHoldService extends ICrudService<RcBdcRouteHoldEntity> {
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
    void removeHoldList(Long[] ids, String reason);
}