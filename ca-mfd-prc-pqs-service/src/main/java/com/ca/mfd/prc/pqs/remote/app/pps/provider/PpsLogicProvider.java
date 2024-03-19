package com.ca.mfd.prc.pqs.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.pqs.remote.app.pps.IPpsLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author edwards.qu
 */
@Service
public class PpsLogicProvider {

    @Autowired
    private IPpsLogicService ppsLogicService;

    public String getPresentShcShiftId(String pmShopId) {
        String result = ppsLogicService.getPresentShcShiftId(pmShopId);
        if (result == null) {
            throw new InkelinkException("服务inkelink-pps-ppsentry调用失败" + result);
        }
        return result;
    }

}