package com.ca.mfd.prc.eps.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import org.dom4j.Document;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * IPmVersionService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmversion", contextId = "inkelink-pm-pmversion")
public interface IPmVersionService {

    @GetMapping(value = "/provider/getobjectedpm")
    ResultVO<PmAllDTO> getObjectedPm();

    @PostMapping(value = "/provider/getcurretpm")
    ResultVO<Document> getCurretPm();
}
