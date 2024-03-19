package com.ca.mfd.prc.eps.remote.app.pqs;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pqs.dto.ActiveAnomalyInfo;
import com.ca.mfd.prc.eps.remote.app.pqs.dto.AnomalyActivity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * IPqsLogicService
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pqs-service", path = "pqslogic", contextId = "inkelink-pqs-pqslogic")
public interface IPqsLogicService {

    @PostMapping("/provider/anomalyactivity")
    ResultVO<String> modifyDefectAnomalyStatus(@RequestBody AnomalyActivity anomalyActivity);

    @PostMapping("/provider/activeanomaly")
    ResultVO<String> activeAnomaly(@RequestBody ActiveAnomalyInfo activeAnomalyInfo);


}
