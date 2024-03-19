package com.ca.mfd.prc.audit.remote.app.pps;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pps-service",
        path = "ppsLogic",
        contextId = "inkelink-pps-ppsLogic")
public interface IPpsLogicService {

    /**
     * 获取当前的班次编号(等待实现)
     *
     * @param pmShopId
     * @return String
     */
    @GetMapping("/getpresentshcshiftid")
    String getPresentShcShiftId(@RequestParam("pmShopId") String pmShopId);
}
