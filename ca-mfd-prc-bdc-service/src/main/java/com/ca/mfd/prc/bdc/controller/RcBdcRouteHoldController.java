package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.dto.RcRouteHoldDTO;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteHoldEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteHoldService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区暂存表Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutehold")
@Tag(name = "路由区暂存表服务", description = "路由区暂存表")
public class RcBdcRouteHoldController extends BaseController<RcBdcRouteHoldEntity> {

    private final IRcBdcRouteHoldService rcBdcRouteHoldService;

    @Autowired
    public RcBdcRouteHoldController(IRcBdcRouteHoldService rcBdcRouteHoldService) {
        this.crudService = rcBdcRouteHoldService;
        this.rcBdcRouteHoldService = rcBdcRouteHoldService;
    }

    @PostMapping("addHoldList")
    @ApiOperation("暂存车辆-后台")
    public ResultVO<String> addHoldList(@RequestBody RcRouteHoldDTO.AddModel model) {
        rcBdcRouteHoldService.addHoldList(model.getAreaId(), model.getSns(), model.getReason());
        rcBdcRouteHoldService.saveChange();
        return new ResultVO<String>().ok("暂存车辆成功", "暂存车辆成功");
    }

    @PostMapping("removeHoldList")
    @ApiOperation("解除暂存车辆-后台")
    public ResultVO<String> removeHoldList(@RequestBody RcRouteHoldDTO.RemoveModel model) {
        //Long[] ids = model.getIds();
        Long[] ids = model.getIds().toArray(new Long[0]);
        rcBdcRouteHoldService.removeHoldList(ids, model.getReason());
        rcBdcRouteHoldService.saveChange();
        return new ResultVO<String>().ok("解除暂存车辆成功", "解除暂存车辆成功");
    }

}