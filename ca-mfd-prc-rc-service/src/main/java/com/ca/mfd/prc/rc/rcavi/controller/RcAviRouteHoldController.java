package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.rc.rcavi.dto.RcRouteHoldDTO;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteHoldEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteHoldService;
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
@RequestMapping("rcaviroutehold")
@Tag(name = "路由区暂存表服务", description = "路由区暂存表")
public class RcAviRouteHoldController extends BaseController<RcAviRouteHoldEntity> {

    private final IRcAviRouteHoldService rcRouteHoldService;

    @Autowired
    public RcAviRouteHoldController(IRcAviRouteHoldService rcRouteHoldService) {
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
        String[] ids = model.getIds();
        rcRouteHoldService.removeHoldList(ids, model.getReason());
        rcRouteHoldService.saveChange();
        return new ResultVO<String>().ok("解除暂存车辆成功", "解除暂存车辆成功");
    }
}