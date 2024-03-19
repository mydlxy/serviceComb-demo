package com.ca.mfd.prc.eps.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmTraceComponentEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * IPmTraceComponentService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmtracecomponent", contextId = "inkelink-pm-pmtracecomponent")
public interface IPmTraceComponentService {
    @GetMapping("/provider/getdatacache")
    ResultVO<List<PmTraceComponentEntity>> getDataCache();

    @PostMapping("/provider/savebybom")
    ResultVO<String> saveByBom(@RequestParam("materialNo") String materialNo, @RequestParam("materialCn") String materialCn);
}
