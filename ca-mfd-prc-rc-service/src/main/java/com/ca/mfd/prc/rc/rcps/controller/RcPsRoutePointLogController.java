package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRoutePointLogEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRoutePointLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点操作履历Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpsroutepointlog")
@Tag(name = "路由点操作履历服务", description = "路由点操作履历")
public class RcPsRoutePointLogController extends BaseController<RcPsRoutePointLogEntity> {

    private final IRcPsRoutePointLogService rcRoutePointLogService;

    @Autowired
    public RcPsRoutePointLogController(IRcPsRoutePointLogService rcRoutePointLogService) {
        this.crudService = rcRoutePointLogService;
        this.rcRoutePointLogService = rcRoutePointLogService;
    }

}