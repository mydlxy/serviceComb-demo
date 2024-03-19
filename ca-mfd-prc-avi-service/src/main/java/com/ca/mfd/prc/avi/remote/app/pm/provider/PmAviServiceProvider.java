package com.ca.mfd.prc.avi.remote.app.pm.provider;

import com.ca.mfd.prc.avi.remote.app.pm.IPmAviService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.AviInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmAviServiceProvider {
    @Autowired
    IPmAviService pmAviService;

    public List<AviInfoDTO> getAviInfos() {
        ResultVO<List<AviInfoDTO>> result = pmAviService.getAviInfos();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmavi调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
