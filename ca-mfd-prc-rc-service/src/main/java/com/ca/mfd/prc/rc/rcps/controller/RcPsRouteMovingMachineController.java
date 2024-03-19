package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteMovingMachineEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteMovingMachineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点移行机信息Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpsroutemovingmachine")
@Tag(name = "路由点移行机信息服务", description = "路由点移行机信息")
public class RcPsRouteMovingMachineController extends BaseController<RcPsRouteMovingMachineEntity> {

    private final IRcPsRouteMovingMachineService rcRouteMovingMachineService;

    @Autowired
    public RcPsRouteMovingMachineController(IRcPsRouteMovingMachineService rcRouteMovingMachineService) {
        this.crudService = rcRouteMovingMachineService;
        this.rcRouteMovingMachineService = rcRouteMovingMachineService;
    }

}