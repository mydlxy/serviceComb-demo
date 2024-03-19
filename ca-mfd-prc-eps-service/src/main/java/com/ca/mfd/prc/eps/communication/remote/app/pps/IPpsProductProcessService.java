package com.ca.mfd.prc.eps.communication.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsProductProcessEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * IPpsEntryReportService
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsproductprocess", contextId = "inkelink-pps-midppsproductprocess")
public interface IPpsProductProcessService {
    @GetMapping(value = "/provider/getalldatas")
    ResultVO<List<PpsProductProcessEntity>> getAllDatas();
}
