package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRouteAreaEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRouteAreaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutearea")
@Tag(name = "路由区服务", description = "路由区")
public class RcSmlRouteAreaController extends BaseController<RcSmlRouteAreaEntity> {

    private final IRcSmlRouteAreaService rcSmlRouteAreaService;

    @Autowired
    public RcSmlRouteAreaController(IRcSmlRouteAreaService rcSmlRouteAreaService) {
        this.crudService = rcSmlRouteAreaService;
        this.rcSmlRouteAreaService = rcSmlRouteAreaService;
    }

}