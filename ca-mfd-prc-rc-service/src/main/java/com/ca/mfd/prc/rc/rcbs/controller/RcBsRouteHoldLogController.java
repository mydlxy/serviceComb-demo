package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteHoldLogEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteHoldLogService;
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
@RequestMapping("rcbsrouteholdlog")
@Tag(name = "路由区暂存日志表服务", description = "路由区暂存日志表")
public class RcBsRouteHoldLogController extends BaseController<RcBsRouteHoldLogEntity> {

    private final IRcBsRouteHoldLogService rcRouteHoldLogService;

    @Autowired
    public RcBsRouteHoldLogController(IRcBsRouteHoldLogService rcRouteHoldLogService) {
        this.crudService = rcRouteHoldLogService;
        this.rcRouteHoldLogService = rcRouteHoldLogService;
    }

}