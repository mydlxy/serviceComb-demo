package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRoutePointEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRoutePointService;
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
@RequestMapping("rcaviroutepoint")
@Tag(name = "路由点服务", description = "路由点")
public class RcAviRoutePointController extends BaseController<RcAviRoutePointEntity> {

    private final IRcAviRoutePointService rcRoutePointService;

    @Autowired
    public RcAviRoutePointController(IRcAviRoutePointService rcRoutePointService) {
        this.crudService = rcRoutePointService;
        this.rcRoutePointService = rcRoutePointService;
    }

}