package com.ca.mfd.prc.avi.communication.remote.app.pps;

import com.ca.mfd.prc.avi.communication.remote.app.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * IPpsOrderService
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsplan", contextId = "inkelink-pps-midppsplan")
public interface IPpsPlanService {
    @GetMapping("/provider/getfirstbyplanno")
    ResultVO<PpsPlanEntity> getFirstByPlanNo(@RequestParam("planNo") String planNo);

    @Operation(summary = "线体是否在计划履历中")
    @GetMapping("/provider/hasplanline")
    ResultVO<Integer> hasPlanLine(@RequestParam("lineCode") String lineCode);
}
