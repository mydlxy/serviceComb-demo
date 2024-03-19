package com.ca.mfd.prc.pmc.remote.app.eps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.eps.IEpsWorkplaceTimeLogService;
import com.ca.mfd.prc.pmc.remote.app.eps.entity.EpsWorkplaceTimeLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpsWorkplaceTimeLogProvider {
    @Autowired
    IEpsWorkplaceTimeLogService epsWorkplaceTimeLogService;

    public List<EpsWorkplaceTimeLogEntity> getBySn(String workstationCode, String sn) {
        ResultVO<List<EpsWorkplaceTimeLogEntity>> result = epsWorkplaceTimeLogService.getBySn(workstationCode, sn);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-epsworkplacetimelog" + result.getMessage());
        }
        return result.getData();
    }
}
