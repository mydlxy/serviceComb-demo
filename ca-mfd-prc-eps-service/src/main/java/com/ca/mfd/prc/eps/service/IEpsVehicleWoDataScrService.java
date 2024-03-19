package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataScrEntity;

import java.util.List;

/**
 * 工艺数据，拧紧
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IEpsVehicleWoDataScrService extends ICrudService<EpsVehicleWoDataScrEntity> {

    /**
     * 获取
     *
     * @param pmcVehicleWoDataId
     * @return
     */
    List<EpsVehicleWoDataScrEntity> getByPmcVehicleWoDataId(Long pmcVehicleWoDataId);
}