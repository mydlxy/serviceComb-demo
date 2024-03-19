package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRouteMovingMachineEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRouteMovingMachineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点移行机信息Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutemovingmachine")
@Tag(name = "路由点移行机信息服务", description = "路由点移行机信息")
public class RcSmlRouteMovingMachineController extends BaseController<RcSmlRouteMovingMachineEntity> {

    private final IRcSmlRouteMovingMachineService rcSmlRouteMovingMachineService;

    @Autowired
    public RcSmlRouteMovingMachineController(IRcSmlRouteMovingMachineService rcSmlRouteMovingMachineService) {
        this.crudService = rcSmlRouteMovingMachineService;
        this.rcSmlRouteMovingMachineService = rcSmlRouteMovingMachineService;
    }

}