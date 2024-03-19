package com.ca.mfd.prc.pmc.remote.app.eps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.eps.entity.EpsWorkplaceTimeLogEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ca-mfd-prc-eps-service",
        path = "epsworkplacetimelog",
        contextId = "inkelink-eps-epsworkplacetimelog")
public interface IEpsWorkplaceTimeLogService {
    @GetMapping("/provider/getbysn")
    ResultVO<List<EpsWorkplaceTimeLogEntity>> getBySn(@RequestParam("workstationCode") String workstationCode,@RequestParam("sn")  String sn);
}
