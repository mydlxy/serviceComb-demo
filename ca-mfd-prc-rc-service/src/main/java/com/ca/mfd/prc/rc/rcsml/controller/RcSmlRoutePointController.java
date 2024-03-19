package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRoutePointEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRoutePointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutepoint")
@Tag(name = "路由点服务", description = "路由点")
public class RcSmlRoutePointController extends BaseController<RcSmlRoutePointEntity> {

    private final IRcSmlRoutePointService rcSmlRoutePointService;

    @Autowired
    public RcSmlRoutePointController(IRcSmlRoutePointService rcSmlRoutePointService) {
        this.crudService = rcSmlRoutePointService;
        this.rcSmlRoutePointService = rcSmlRoutePointService;
    }

}