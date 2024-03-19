package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRoutePointLogEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRoutePointLogService;
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
@RequestMapping("rcaviroutepointlog")
@Tag(name = "路由点操作履历服务", description = "路由点操作履历")
public class RcAviRoutePointLogController extends BaseController<RcAviRoutePointLogEntity> {

    private final IRcAviRoutePointLogService rcRoutePointLogService;

    @Autowired
    public RcAviRoutePointLogController(IRcAviRoutePointLogService rcRoutePointLogService) {
        this.crudService = rcRoutePointLogService;
        this.rcRoutePointLogService = rcRoutePointLogService;
    }

}