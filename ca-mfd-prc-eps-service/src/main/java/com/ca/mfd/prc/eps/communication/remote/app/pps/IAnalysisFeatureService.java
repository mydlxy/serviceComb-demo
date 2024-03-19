package com.ca.mfd.prc.eps.communication.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pps.dto.FilterFetureExpressionPara;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * IAnalysisFeatureService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "analysisfeture", contextId = "inkelink-pps-midanalysisfeture")
public interface IAnalysisFeatureService {

    @PostMapping("filterfetureexpression")
    ResultVO<List<String>> filterFeatureExpression(@RequestBody FilterFetureExpressionPara para);
}
