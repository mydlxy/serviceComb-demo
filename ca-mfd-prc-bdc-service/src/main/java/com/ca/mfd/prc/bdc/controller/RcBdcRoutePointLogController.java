package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.entity.RcBdcRoutePointLogEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcRoutePointLogService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点操作履历Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutepointlog")
@Tag(name = "路由点操作履历服务", description = "路由点操作履历")
public class RcBdcRoutePointLogController extends BaseController<RcBdcRoutePointLogEntity> {

    private final IRcBdcRoutePointLogService rcBdcRoutePointLogService;

    @Autowired
    public RcBdcRoutePointLogController(IRcBdcRoutePointLogService rcBdcRoutePointLogService) {
        this.crudService = rcBdcRoutePointLogService;
        this.rcBdcRoutePointLogService = rcBdcRoutePointLogService;
    }

}