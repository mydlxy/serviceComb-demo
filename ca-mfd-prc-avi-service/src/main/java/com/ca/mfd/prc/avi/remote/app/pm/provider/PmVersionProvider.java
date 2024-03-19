package com.ca.mfd.prc.avi.remote.app.pm.provider;

import com.ca.mfd.prc.avi.remote.app.pm.IPmVersionService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PmVersionProvider {
    @Autowired
    IPmVersionService pmVersionService;

    public PmAllDTO getObjectedPm() {
        ResultVO<PmAllDTO> result = pmVersionService.getObjectedPm();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }
}

