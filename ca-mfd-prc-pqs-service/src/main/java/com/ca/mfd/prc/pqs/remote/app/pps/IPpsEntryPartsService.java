package com.ca.mfd.prc.pqs.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryPartsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pps-service",
        path = "ppsentryparts",
        contextId = "inkelink-pps-ppsentryparts")
public interface IPpsEntryPartsService {

    /**
     * 获取工单-零部件信息
     *
     * @param entryNo
     * @return
     */
    @GetMapping(value = "/provider/getentrypartsinfobyentryno")
    ResultVO<PpsEntryPartsEntity> getEntryPartsInfoByEntryNo(@RequestParam("entryNo") String entryNo);

    /**
     * 获取工单-零部件信息
     *
     * @param planNoOrEntryNo
     * @return
     */
    @GetMapping(value = "/provider/getfirstbyplannoorentryno")
    ResultVO<PpsEntryPartsEntity> getFirstByPlanNoOrEntryNo(@RequestParam("planNoOrEntryNo") String planNoOrEntryNo);
}
