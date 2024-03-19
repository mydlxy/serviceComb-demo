package com.ca.mfd.prc.eps.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * IPpsEntryReportService
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsentryreport", contextId = "inkelink-pps-ppsentryreport")
public interface IPpsEntryReportService {
    @PostMapping("/provider/printentryreport")
    ResultVO printEntryReport(@RequestBody List<String> reportIds);
}
