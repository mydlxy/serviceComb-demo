package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRoutePointEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRoutePointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpsroutepoint")
@Tag(name = "路由点服务", description = "路由点")
public class RcPsRoutePointController extends BaseController<RcPsRoutePointEntity> {

    private final IRcPsRoutePointService rcRoutePointService;

    @Autowired
    public RcPsRoutePointController(IRcPsRoutePointService rcRoutePointService) {
        this.crudService = rcRoutePointService;
        this.rcRoutePointService = rcRoutePointService;
    }
}