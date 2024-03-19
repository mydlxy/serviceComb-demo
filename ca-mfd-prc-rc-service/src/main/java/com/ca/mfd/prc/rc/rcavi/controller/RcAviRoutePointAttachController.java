package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRoutePointAttachEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRoutePointAttachService;
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
@RequestMapping("rcaviroutepointattach")
@Tag(name = "路由点关联附加模块服务", description = "路由点关联附加模块")
public class RcAviRoutePointAttachController extends BaseController<RcAviRoutePointAttachEntity> {

    private final IRcAviRoutePointAttachService rcAviRoutePointAttachService;

    @Autowired
    public RcAviRoutePointAttachController(IRcAviRoutePointAttachService rcAviRoutePointAttachService) {
        this.crudService = rcAviRoutePointAttachService;
        this.rcAviRoutePointAttachService = rcAviRoutePointAttachService;
    }

}