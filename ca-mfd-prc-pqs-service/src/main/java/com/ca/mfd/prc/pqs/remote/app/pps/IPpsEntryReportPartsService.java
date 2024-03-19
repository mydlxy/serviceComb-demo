package com.ca.mfd.prc.pqs.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryReportPartsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pps-service",
        path = "ppsentryreportparts",
        contextId = "inkelink-pps-ppsentryreportparts")
public interface IPpsEntryReportPartsService {

    /**
     * 获取报工单-零部件信息
     *
     * @param barcode
     * @return
     */
    @GetMapping(value = "/provider/getfirstbybarcode")
    ResultVO<PpsEntryReportPartsEntity> getFirstByBarcode(@RequestParam("barcode") String barcode);
}
