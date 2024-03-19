package com.ca.mfd.prc.rc.rcbdc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbdc.entity.RcBdcRouteAreaAttachEntity;
import com.ca.mfd.prc.rc.rcbdc.service.IRcBdcRouteAreaAttachService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点关联附加模块Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcrouteareaattach")
@Tag(name = "路由点关联附加模块服务", description = "路由点关联附加模块")
public class RcBdcRouteAreaAttachController extends BaseController<RcBdcRouteAreaAttachEntity> {

    private final IRcBdcRouteAreaAttachService rcBdcRouteAreaAttachService;

    @Autowired
    public RcBdcRouteAreaAttachController(IRcBdcRouteAreaAttachService rcBdcRouteAreaAttachService) {
        this.crudService = rcBdcRouteAreaAttachService;
        this.rcBdcRouteAreaAttachService = rcBdcRouteAreaAttachService;
    }

}