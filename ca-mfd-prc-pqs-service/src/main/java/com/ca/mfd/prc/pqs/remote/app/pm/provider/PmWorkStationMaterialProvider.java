package com.ca.mfd.prc.pqs.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pm.IPmWorkStationMaterialService;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmWorkstationMaterialEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author edwards.qu
 */
@Service
public class PmWorkStationMaterialProvider {

    @Autowired
    private IPmWorkStationMaterialService pmWorkStationMaterialService;

    public List<PmWorkstationMaterialEntity> getByWorkstationCode(String workstationCode) {
        ResultVO<List<PmWorkstationMaterialEntity>> result = pmWorkStationMaterialService.getByWorkstationCode(workstationCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmworkstationmaterial调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
