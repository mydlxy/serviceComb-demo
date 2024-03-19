package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRouteHoldEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRouteHoldService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区HOLD车Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutehold")
@Tag(name = "路由区HOLD车服务", description = "路由区HOLD车")
public class RcSmlRouteHoldController extends BaseController<RcSmlRouteHoldEntity> {

    private final IRcSmlRouteHoldService rcSmlRouteHoldService;

    @Autowired
    public RcSmlRouteHoldController(IRcSmlRouteHoldService rcSmlRouteHoldService) {
        this.crudService = rcSmlRouteHoldService;
        this.rcSmlRouteHoldService = rcSmlRouteHoldService;
    }

}