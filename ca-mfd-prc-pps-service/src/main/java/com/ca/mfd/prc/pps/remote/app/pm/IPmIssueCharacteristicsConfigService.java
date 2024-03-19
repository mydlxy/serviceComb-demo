package com.ca.mfd.prc.pps.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmIssueCharacteristicsConfigEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: BOM详细
 * @date 2023年6月7日
 * @变更说明 BY eric.zhou At 2023年6月7日
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmissuecharacteristicsconfig", contextId = "inkelink-pm-pmissuecharacteristicsconfig")
public interface IPmIssueCharacteristicsConfigService {

    /**
     * 下发特征配置
     *
     * @param subKey
     * @param relevanceType
     * @return 下发特征配置
     */
    @GetMapping(value = "/provider/getlistbysubkeyrelevancetype")
    ResultVO<List<PmIssueCharacteristicsConfigEntity>> getListBySubKeyRelevanceType(@RequestParam("subKey") String subKey, @RequestParam("relevanceType") Integer relevanceType);

}
