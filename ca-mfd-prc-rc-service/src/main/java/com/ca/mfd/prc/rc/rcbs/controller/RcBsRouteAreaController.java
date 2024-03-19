package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteAreaEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteAreaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcbsroutearea")
@Tag(name = "路由区服务", description = "路由区")
public class RcBsRouteAreaController extends BaseController<RcBsRouteAreaEntity> {

    private final IRcBsRouteAreaService rcRouteAreaService;

    @Autowired
    public RcBsRouteAreaController(IRcBsRouteAreaService rcRouteAreaService) {
        this.crudService = rcRouteAreaService;
        this.rcRouteAreaService = rcRouteAreaService;
    }

}