package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteMovingMachineEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteMovingMachineService;
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
@RequestMapping("rcbsroutemovingmachine")
@Tag(name = "路由点移行机信息服务", description = "路由点移行机信息")
public class RcBsRouteMovingMachineController extends BaseController<RcBsRouteMovingMachineEntity> {

    private final IRcBsRouteMovingMachineService rcRouteMovingMachineService;

    @Autowired
    public RcBsRouteMovingMachineController(IRcBsRouteMovingMachineService rcRouteMovingMachineService) {
        this.crudService = rcRouteMovingMachineService;
        this.rcRouteMovingMachineService = rcRouteMovingMachineService;
    }

}