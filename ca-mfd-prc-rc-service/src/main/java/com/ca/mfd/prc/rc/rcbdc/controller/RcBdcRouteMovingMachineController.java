package com.ca.mfd.prc.rc.rcbdc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbdc.entity.RcBdcRouteMovingMachineEntity;
import com.ca.mfd.prc.rc.rcbdc.service.IRcBdcRouteMovingMachineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点移行机信息Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutemovingmachine")
@Tag(name = "路由点移行机信息服务", description = "路由点移行机信息")
public class RcBdcRouteMovingMachineController extends BaseController<RcBdcRouteMovingMachineEntity> {

    private final IRcBdcRouteMovingMachineService rcBdcRouteMovingMachineService;

    @Autowired
    public RcBdcRouteMovingMachineController(IRcBdcRouteMovingMachineService rcBdcRouteMovingMachineService) {
        this.crudService = rcBdcRouteMovingMachineService;
        this.rcBdcRouteMovingMachineService = rcBdcRouteMovingMachineService;
    }

}