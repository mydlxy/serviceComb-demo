package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.rc.rcps.dto.RcRouteHoldDTO;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteHoldEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteHoldService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区暂存表Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpsroutehold")
@Tag(name = "路由区暂存表服务", description = "路由区暂存表")
public class RcPsRouteHoldController extends BaseController<RcPsRouteHoldEntity> {

    private final IRcPsRouteHoldService rcRouteHoldService;

    @Autowired
    public RcPsRouteHoldController(IRcPsRouteHoldService rcRouteHoldService) {
        this.crudService = rcRouteHoldService;
        this.rcRouteHoldService = rcRouteHoldService;
    }

    @PostMapping("addholdlist")
    @ApiOperation("暂存车辆-后台")
    @Operation(summary = "暂存车辆-后台")
    public ResultVO<String> addHoldList(@RequestBody RcRouteHoldDTO.AddModel model) {
        rcRouteHoldService.addHoldList(model.getAreaId(), model.getSns(), model.getReason());
        rcRouteHoldService.saveChange();
        return new ResultVO<String>().ok("暂存车辆成功", "暂存车辆成功");
    }

    @PostMapping("removeholdlist")
    @ApiOperation("解除暂存车辆-后台")
    @Operation(summary = "解除暂存车辆-后台")
    public ResultVO<String> removeHoldList(@RequestBody RcRouteHoldDTO.RemoveModel model) {
        //Long[] ids = model.getIds();
        Long[] ids = model.getIds().toArray(new Long[0]);
        rcRouteHoldService.removeHoldList(ids, model.getReason());
        rcRouteHoldService.saveChange();
        return new ResultVO<String>().ok("解除暂存车辆成功", "解除暂存车辆成功");
    }
}