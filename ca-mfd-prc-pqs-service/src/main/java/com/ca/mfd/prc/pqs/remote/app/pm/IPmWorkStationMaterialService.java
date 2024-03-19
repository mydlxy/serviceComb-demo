package com.ca.mfd.prc.pqs.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmWorkstationMaterialEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pm-service",
        path = "pmworkstationmaterial",
        contextId = "inkelink-pm-pmworkstationmaterial")
public interface IPmWorkStationMaterialService {

    /**
     * getbyworkstationcode
     *
     * @param workstationCode
     * @return
     */
    @GetMapping("/provider/getbyworkstationcode")
    ResultVO<List<PmWorkstationMaterialEntity>> getByWorkstationCode(@RequestParam("workstationCode") String workstationCode);
}
