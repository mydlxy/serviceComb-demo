package com.ca.mfd.prc.audit.remote.app.pm;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pm-service",
        path = "pmworkstation",
        contextId = "inkelink-pm-pmworkstation")
public interface IPmWorkStationService {

    /**
     * getCurrentWorkplaceList
     *
     * @param pageIndex
     * @param pageSize
     * @param conditions
     * @return
     */
    @GetMapping("/provider/getcurrentworkplacelist")
    ResultVO getCurrentWorkplaceList(@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize, @RequestBody List<ConditionDto> conditions);
}
