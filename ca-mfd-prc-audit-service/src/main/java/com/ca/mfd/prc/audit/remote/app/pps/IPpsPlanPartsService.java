package com.ca.mfd.prc.audit.remote.app.pps;

import com.ca.mfd.prc.audit.remote.app.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pps-service",
        path = "ppsplanparts",
        contextId = "inkelink-pps-ppsplanparts")
public interface IPpsPlanPartsService {

    /**
     * 获取生产计划-零部件信息
     *
     * @param planNo
     * @return
     */
    @GetMapping(value = "/provider/getplanpastsbyplanno")
    ResultVO<PpsPlanPartsEntity> getPlanPastsByPlanNo(@RequestParam("planNo") String planNo);
}
