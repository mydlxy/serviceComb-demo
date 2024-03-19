package com.ca.mfd.prc.pps.remote.app.pm;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.pm.dto.ShcWorkTimeInfo;
import com.ca.mfd.prc.pps.remote.app.pm.dto.ShcWorkTimePara;
import com.ca.mfd.prc.pps.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmShcCalendarEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: joel
 * @Date: 2023-08-31-17:28
 * @Description:
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmshccalendar", contextId = "inkelink-pm-pmshccalendar")
public interface IPmShcCalendarService {

    /**
     * 获取未逻辑删除的列表数据
     *
     * @param conditions 条件表达式
     * @return List<PpsShcCalendarEntity>
     */
    @PostMapping("/provider/getdata")
    ResultVO<List<PmShcCalendarEntity>> getData(@RequestBody List<ConditionDto> conditions);

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
     * @param para
     * @return
     */
    @PostMapping("/provider/getWorkTimes")
    ResultVO<List<ShcWorkTimeInfo>> getWorkTimes(@RequestBody ShcWorkTimePara para);

}
