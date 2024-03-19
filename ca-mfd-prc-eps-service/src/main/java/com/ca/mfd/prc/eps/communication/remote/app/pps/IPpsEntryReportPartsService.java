package com.ca.mfd.prc.eps.communication.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsEntryReportPartsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * IPpsEntryReportService
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsentryreportparts", contextId = "inkelink-pps-midppsentryreportparts")
public interface IPpsEntryReportPartsService {
    @GetMapping("/provider/getfirstbybarcode")
    ResultVO<PpsEntryReportPartsEntity> getFirstByBarcode(@RequestParam("barcode") String barcode);
}
