package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteHoldLogEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteHoldLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区暂存日志表Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpsrouteholdlog")
@Tag(name = "路由区暂存日志表服务", description = "路由区暂存日志表")
public class RcPsRouteHoldLogController extends BaseController<RcPsRouteHoldLogEntity> {

    private final IRcPsRouteHoldLogService rcRouteHoldLogService;

    @Autowired
    public RcPsRouteHoldLogController(IRcPsRouteHoldLogService rcRouteHoldLogService) {
        this.crudService = rcRouteHoldLogService;
        this.rcRouteHoldLogService = rcRouteHoldLogService;
    }

}