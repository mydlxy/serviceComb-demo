package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.dto.CreateAndonAlarmDTO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmComponentAlarmEntity;
import com.ca.mfd.prc.pmc.service.IPmcAlarmComponentAlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 报警原始记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("pmcalarmcomponentalarm")
@Tag(name = "报警原始记录")
public class PmcAlarmComponentAlarmController extends BaseController<PmcAlarmComponentAlarmEntity> {

    private final IPmcAlarmComponentAlarmService pmcAlarmComponentAlarmService;

    @Autowired
    public PmcAlarmComponentAlarmController(IPmcAlarmComponentAlarmService pmcAlarmComponentAlarmService) {
        this.crudService = pmcAlarmComponentAlarmService;
        this.pmcAlarmComponentAlarmService = pmcAlarmComponentAlarmService;
    }

    @Operation(summary = "获取一个工段内并且一段时间内所有的报警信息")
    @GetMapping("getalarmcomponentalarmlist")
    public ResultVO getAlarmComponentAlarmList(String startTime, String endTime, String postion) {
        List<PmcAlarmComponentAlarmEntity> alarmComponentAlarmList = pmcAlarmComponentAlarmService.getAlarmComponentAlarmList(startTime, endTime, postion);
        return new ResultVO<List<PmcAlarmComponentAlarmEntity>>().ok(alarmComponentAlarmList);
    }

    @Operation(summary = "添加安灯报警时通知给报警模块")
    @PostMapping("addandonalarm")
    public ResultVO addAndonAlarm(@RequestBody CreateAndonAlarmDTO para) {
        pmcAlarmComponentAlarmService.addAndonAlarm(para);
        return new ResultVO<String>().ok("", "操作成功");
    }

}