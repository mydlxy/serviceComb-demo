package com.ca.mfd.prc.pqs.remote.app.pps;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pps-service",
        path = "ppsentry",
        contextId = "inkelink-pps-ppsentry")
public interface IPpsEntryService {

    @PostMapping(value = "/getdata")
    ResultVO<List<PpsEntryEntity>> getData(@RequestBody List<ConditionDto> conditions);
}
