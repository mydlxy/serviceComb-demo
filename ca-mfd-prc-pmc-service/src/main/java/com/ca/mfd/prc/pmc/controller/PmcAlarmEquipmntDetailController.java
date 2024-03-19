package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.dto.GetEquipmntdetailPara;
import com.ca.mfd.prc.pmc.dto.TeamleaderFillInfoDTO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmEquipmntDetailEntity;
import com.ca.mfd.prc.pmc.service.IPmcAlarmEquipmntDetailService;
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
 * 单个设备报警数据沉淀
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("pmcalarmequipmntdetail")
@Tag(name = "单个设备报警数据沉淀")
public class PmcAlarmEquipmntDetailController extends BaseController<PmcAlarmEquipmntDetailEntity> {

    @Autowired
    private IPmcAlarmEquipmntDetailService pmcAlarmEquipmntDetailService;

    @Autowired
    public PmcAlarmEquipmntDetailController(IPmcAlarmEquipmntDetailService pmcAlarmEquipmntDetailService) {
        this.crudService = pmcAlarmEquipmntDetailService;
        this.pmcAlarmEquipmntDetailService = pmcAlarmEquipmntDetailService;
    }

    @Operation(summary = "获取一个工段内并且一段时间内所有的报警信息")
    @PostMapping("getlist")
    public ResultVO getList(@RequestBody GetEquipmntdetailPara para) {
        List<PmcAlarmEquipmntDetailEntity> alarmEquipmntDetailEntityList = pmcAlarmEquipmntDetailService.getAlarmDetailList(para.getShopCode(), para.getStartTime(), para.getEndTime(), para.getPosition());
        return new ResultVO<List<PmcAlarmEquipmntDetailEntity>>().ok(alarmEquipmntDetailEntityList);
    }

    @Operation(summary = "获取班组长需要处理的问题")
    @GetMapping("getteamleaderdata")
    public ResultVO getTeamleaderData(String userId) {
        List<PmcAlarmEquipmntDetailEntity> alarmEquipmntDetailEntityList = pmcAlarmEquipmntDetailService.getTeamleaderData(userId);
        return new ResultVO<List<PmcAlarmEquipmntDetailEntity>>().ok(alarmEquipmntDetailEntityList);
    }

    @Operation(summary = "班组长处理报警问题")
    @PostMapping("teamleaderfill")
    public ResultVO<String> teamleaderFill(@RequestBody TeamleaderFillInfoDTO para) {
        pmcAlarmEquipmntDetailService.teamleaderFill(para);
        return new ResultVO<String>().ok("操作成功");
    }

}