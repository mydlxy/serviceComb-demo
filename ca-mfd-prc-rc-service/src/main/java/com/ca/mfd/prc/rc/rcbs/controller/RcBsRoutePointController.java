package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePointEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRoutePointService;
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
@RequestMapping("rcbsroutepoint")
@Tag(name = "路由点服务", description = "路由点")
public class RcBsRoutePointController extends BaseController<RcBsRoutePointEntity> {

    private final IRcBsRoutePointService rcRoutePointService;

    @Autowired
    public RcBsRoutePointController(IRcBsRoutePointService rcRoutePointService) {
        this.crudService = rcRoutePointService;
        this.rcRoutePointService = rcRoutePointService;
    }

}