package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteAreaEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteAreaService;
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
@RequestMapping("rcaviroutearea")
@Tag(name = "路由区服务", description = "路由区")
public class RcAviRouteAreaController extends BaseController<RcAviRouteAreaEntity> {

    private final IRcAviRouteAreaService rcRouteAreaService;

    @Autowired
    public RcAviRouteAreaController(IRcAviRouteAreaService rcRouteAreaService) {
        this.crudService = rcRouteAreaService;
        this.rcRouteAreaService = rcRouteAreaService;
    }

}