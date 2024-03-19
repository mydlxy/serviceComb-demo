package com.ca.mfd.prc.eps.communication.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.remote.app.pps.entity.PpsPlanEntity;
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
    @GetMapping("/provider/getPlanByPlanNo")
    ResultVO<PpsPlanEntity> getFirstByPlanNo(@RequestParam("planNo") String planNo);
}
