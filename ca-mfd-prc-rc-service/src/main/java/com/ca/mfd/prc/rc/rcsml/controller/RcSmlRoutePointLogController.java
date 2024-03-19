package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRoutePointLogEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRoutePointLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点操作履历Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutepointlog")
@Tag(name = "路由点操作履历服务", description = "路由点操作履历")
public class RcSmlRoutePointLogController extends BaseController<RcSmlRoutePointLogEntity> {

    private final IRcSmlRoutePointLogService rcSmlRoutePointLogService;

    @Autowired
    public RcSmlRoutePointLogController(IRcSmlRoutePointLogService rcSmlRoutePointLogService) {
        this.crudService = rcSmlRoutePointLogService;
        this.rcSmlRoutePointLogService = rcSmlRoutePointLogService;
    }

}