package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRouteAreaAttachEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRouteAreaAttachService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点关联附加模块Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlrouteareaattach")
@Tag(name = "路由点关联附加模块服务", description = "路由点关联附加模块")
public class RcSmlRouteAreaAttachController extends BaseController<RcSmlRouteAreaAttachEntity> {

    private final IRcSmlRouteAreaAttachService rcSmlRouteAreaAttachService;

    @Autowired
    public RcSmlRouteAreaAttachController(IRcSmlRouteAreaAttachService rcSmlRouteAreaAttachService) {
        this.crudService = rcSmlRouteAreaAttachService;
        this.rcSmlRouteAreaAttachService = rcSmlRouteAreaAttachService;
    }

}