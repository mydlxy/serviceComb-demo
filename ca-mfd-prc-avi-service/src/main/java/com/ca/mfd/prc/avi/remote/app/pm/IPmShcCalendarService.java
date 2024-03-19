package com.ca.mfd.prc.avi.remote.app.pm;


import com.ca.mfd.prc.avi.remote.app.pm.dto.ShcCalendarDetailInfo;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.ShiftDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmshccalendar", contextId = "inkelink-pm-pmshccalendar")
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
     * 获取排班信息
     *
     * @param lineCode 线体编码
     * @return 排班信息
     */
    @GetMapping("/provider/getcurrentshift")
    ResultVO<ShcCalendarDetailInfo> getCurrentShift(@RequestParam("lineCode") String lineCode);
}
