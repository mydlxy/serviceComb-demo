package com.ca.mfd.prc.pmc.remote.app.pm;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmTeamLeaderConfigEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author mason
 */
@FeignClient(name = "ca-mfd-prc-pm-service",
        path = "pmteamleaderconfig",
        contextId = "inkelink-pm-pmteamleaderconfig")
public interface IPmTeamLeaderConfigService {

    /**
     * 查询班组长配置
     *
     * @param conditionInfos 条件
     * @return 查询结果
     */
    @PostMapping(value = "/provider/getdata")
    ResultVO<List<PmTeamLeaderConfigEntity>> getData(@RequestBody List<ConditionDto> conditionInfos);
}
