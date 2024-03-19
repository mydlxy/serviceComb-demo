package com.ca.mfd.prc.rc.rcbdc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbdc.entity.RcBdcRouteHoldEntity;
import com.ca.mfd.prc.rc.rcbdc.service.IRcBdcRouteHoldService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

}