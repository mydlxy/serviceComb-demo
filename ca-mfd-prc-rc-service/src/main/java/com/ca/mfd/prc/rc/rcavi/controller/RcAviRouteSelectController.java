package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteSelectEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteSelectService;
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
@RequestMapping("rcavirouteselect")
@Tag(name = "路由选择配置服务", description = "路由选择配置")
public class RcAviRouteSelectController extends BaseController<RcAviRouteSelectEntity> {

    private final IRcAviRouteSelectService rcAviRouteSelectService;

    @Autowired
    public RcAviRouteSelectController(IRcAviRouteSelectService rcAviRouteSelectService) {
        this.crudService = rcAviRouteSelectService;
        this.rcAviRouteSelectService = rcAviRouteSelectService;
    }

}