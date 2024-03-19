package com.ca.mfd.prc.eps.communication.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.remote.app.pps.IPpsVehicleMasterProcessService;
import com.ca.mfd.prc.eps.communication.remote.app.pps.entity.MidVehicleMasterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PpsProductProcessProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service("MidPpsVehicleProcessProvider")
public class PpsVehicleMasterProcessProvider {

    @Autowired
    private IPpsVehicleMasterProcessService ppsVehicleMasterProcessService;

    /**
     * 根据参数查询车型主数据
     * @param vehicleMaterialNumber
     * @param bomRoom
     * @return
     */
    public MidVehicleMasterEntity getVehicleMasterByParam(String vehicleMaterialNumber,String bomRoom) {
        ResultVO<MidVehicleMasterEntity> result = ppsVehicleMasterProcessService.getVehicleMasterByParam(vehicleMaterialNumber,bomRoom);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-vehiclemasterprocess调用失败" + result.getMessage());
        }
        return result.getData();
    }

}