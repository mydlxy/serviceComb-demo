package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentParConfigEntity;

import java.util.List;

/**
 * 追溯设备工艺参数
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IEpsVehicleEqumentParConfigService extends ICrudService<EpsVehicleEqumentParConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsVehicleEqumentParConfigEntity> getAllDatas();
}