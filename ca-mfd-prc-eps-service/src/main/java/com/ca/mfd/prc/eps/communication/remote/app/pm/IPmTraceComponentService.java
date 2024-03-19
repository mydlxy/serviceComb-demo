package com.ca.mfd.prc.eps.communication.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmTraceComponentEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * IPmTraceComponentService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmtracecomponent", contextId = "inkelink-pm-midpmtracecomponent")
public interface IPmTraceComponentService {
    @GetMapping("/provider/getdatacache")
    ResultVO<List<PmTraceComponentEntity>> getDataCache();
}
