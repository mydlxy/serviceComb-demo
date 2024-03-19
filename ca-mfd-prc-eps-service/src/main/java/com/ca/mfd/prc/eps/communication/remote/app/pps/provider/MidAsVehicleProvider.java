package com.ca.mfd.prc.eps.communication.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.remote.app.pps.IMidAsVehicleService;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.MidAsVehicleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * MidAsVehicleProvider
 * @author mason
 */
@Service("MidAsVehicleProvider")
public class MidAsVehicleProvider {

    @Autowired
    private IMidAsVehicleService midAsVehicleService;

    /**
     * 获取
     *
     * @param planNo
     * @return
     */
    public MidAsVehicleDto getVehicleByPlanNo(String planNo) {
        ResultVO<MidAsVehicleDto> result = midAsVehicleService.getVehicleByPlanNo(planNo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsplan调用失败" + result.getMessage());
        }
        return result.getData();
    }

}