package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.dto.VehicleModelDto;
import com.ca.mfd.prc.pps.communication.entity.MidVehicleMasterEntity;

import java.util.List;

/**
 *
 * @Description: 车型主数据中间表服务
 * @author inkelink
 * @date 2023年11月02日
 * @变更说明 BY inkelink At 2023年11月02日
 */
public interface IMidVehicleMasterService extends ICrudService<MidVehicleMasterEntity> {


    /**
     * 获取全部数据
     * @return
     */
    List<MidVehicleMasterEntity> getAllDatas();
    /**
     * 通过调度获取车型数据
     *
     * @param
     * @return
     */
    void receive();

    /**
     * 通过整车物料号获取车型
     * @param materialNo
     * @return
     */

    List<VehicleModelDto> getVehicleModelData(String materialNo);

    /**
     * 同步车型数据到系统配置中
     */
    void excute();

    MidVehicleMasterEntity getVehicleMasterByParam(String vehicleMaterialNumber,String bomRoom);

}