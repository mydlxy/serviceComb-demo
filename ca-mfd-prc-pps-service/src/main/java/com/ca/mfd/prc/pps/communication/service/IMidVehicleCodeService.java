package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.entity.MidVehicleCodeEntity;

import java.util.List;

/**
 *
 * @Description: 车型代码中间表服务
 * @author inkelink
 * @date 2023年12月08日
 * @变更说明 BY inkelink At 2023年12月08日
 */
public interface IMidVehicleCodeService extends ICrudService<MidVehicleCodeEntity> {
    /**
     * 获取全部数据
     * @return
     */
    List<MidVehicleCodeEntity> getAllDatas();

    /**
     * 获取车型代码
     */
    void receive();
}