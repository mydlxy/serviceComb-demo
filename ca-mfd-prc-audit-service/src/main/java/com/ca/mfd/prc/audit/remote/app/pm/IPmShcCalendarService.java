package com.ca.mfd.prc.audit.remote.app.pm;

import com.ca.mfd.prc.audit.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author edwards.qu
 */
@FeignClient(name = "ca-mfd-prc-pm-service",
        path = "pmshccalendar",
        contextId = "inkelink-pm-pmshccalendar")
public interface IPmShcCalendarService {

    /**
     * 获取车间当前班次
     *
     * @param lineCode 线体
     * @return ShiftDTO
     */
    @GetMapping("/provider/getcurrentshiftinfo")
    ResultVO<ShiftDTO> getCurrentShiftInfo(@RequestParam("lineCode") String lineCode);
}
