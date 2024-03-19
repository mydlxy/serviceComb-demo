package com.ca.mfd.prc.eps.remote.app.pqs;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pqs.entity.PqsProductDefectAnomalyEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * IPqsProductDefectAnomalyService
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pqs-service", path = "pqsproductdefectanomaly", contextId = "inkelink-pqs-pqsproductdefectanomaly")
public interface IPqsProductDefectAnomalyService {

    @PostMapping("/provider/getdata")
    ResultVO<List<PqsProductDefectAnomalyEntity>> getData(@RequestBody List<ConditionDto> conditions);
}
