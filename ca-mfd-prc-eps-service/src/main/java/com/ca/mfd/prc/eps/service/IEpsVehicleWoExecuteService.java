package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoExecuteEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 车辆操作执行信息服务
 * @date 2023年09月14日
 * @变更说明 BY inkelink At 2023年09月14日
 */
public interface IEpsVehicleWoExecuteService extends ICrudService<EpsVehicleWoExecuteEntity> {
    /**
     * 获取
     *
     * @param stationCode
     * @param sn
     * @return
     */
    List<EpsVehicleWoExecuteEntity> getListByStationSn(String stationCode, String sn);
}