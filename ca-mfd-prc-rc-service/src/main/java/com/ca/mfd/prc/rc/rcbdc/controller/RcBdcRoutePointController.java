package com.ca.mfd.prc.rc.rcbdc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbdc.entity.RcBdcRoutePointEntity;
import com.ca.mfd.prc.rc.rcbdc.service.IRcBdcRoutePointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutepoint")
@Tag(name = "路由点服务", description = "路由点")
public class RcBdcRoutePointController extends BaseController<RcBdcRoutePointEntity> {

    private final IRcBdcRoutePointService rcBdcRoutePointService;

    @Autowired
    public RcBdcRoutePointController(IRcBdcRoutePointService rcBdcRoutePointService) {
        this.crudService = rcBdcRoutePointService;
        this.rcBdcRoutePointService = rcBdcRoutePointService;
    }

}