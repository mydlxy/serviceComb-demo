package com.ca.mfd.prc.pmc.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.pm.dto.ShiftDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mason
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

    /**
     * 工厂日历
     *
     * @param startTime
     * @param endTime
     * @param shopCode
     * @return Integer
     */
    @GetMapping("/provider/calculateworktime")
    ResultVO<Integer> calculateWorkTime(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("shopCode") String shopCode);
}
