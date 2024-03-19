package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.entity.RcBdcRouteAreaEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteAreaService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutearea")
@Tag(name = "路由区服务", description = "路由区")
public class RcBdcRouteAreaController extends BaseController<RcBdcRouteAreaEntity> {

    private final IRcBdcRouteAreaService rcBdcRouteAreaService;

    @Autowired
    public RcBdcRouteAreaController(IRcBdcRouteAreaService rcBdcRouteAreaService) {
        this.crudService = rcBdcRouteAreaService;
        this.rcBdcRouteAreaService = rcBdcRouteAreaService;
    }

}