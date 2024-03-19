package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.dto.MidAsVehicleDto;
import com.ca.mfd.prc.pps.communication.entity.MidAsVehicleEntity;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductMaterialMasterEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: AS整车信息服务
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
public interface IMidAsVehicleService extends ICrudService<MidAsVehicleEntity> {

    /**
     * 获取整车物料号
     *
     * @param logid
     * @return
     */
    List<String> getvehicleMaterial(Long logid);

    /**
     * 执行数据处理逻辑
     */
    String excute(String logid);

    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    List<MidAsVehicleEntity> getListByLog(Long logid);

    /**
     * 根据计划单号取AS整车信息
     * @param planNo
     * @return
     */
    MidAsVehicleDto getVehicleByPlanNo(String planNo);

}