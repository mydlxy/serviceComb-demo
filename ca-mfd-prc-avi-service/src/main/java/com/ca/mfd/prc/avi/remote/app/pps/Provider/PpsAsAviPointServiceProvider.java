package com.ca.mfd.prc.avi.remote.app.pps.Provider;

import com.ca.mfd.prc.avi.host.scheduling.dto.InsertAsAviPointInfo;
import com.ca.mfd.prc.avi.remote.app.pps.IPpsAsAviPointService;
import com.ca.mfd.prc.avi.remote.app.pps.IPpsEntryService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PpsAsAviPointServiceProvider {
    @Autowired
    IPpsAsAviPointService ppsAsAviPointService;

    public String insertAsAviPoint(InsertAsAviPointInfo datas) {
        ResultVO<String> result = ppsAsAviPointService.insertAsAviPoint(datas);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-aviasavipoint调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String insertDataAsAviPoint(String vehicleSn, String aviCode, int AviType) {
        ResultVO<String> result = ppsAsAviPointService.insertDataAsAviPoint(vehicleSn, aviCode, AviType);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-aviasavipoint调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
