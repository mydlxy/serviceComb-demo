package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.entity.RcBdcRouteHoldLogEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteHoldLogService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区暂存日志表Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcrouteholdlog")
@Tag(name = "路由区暂存日志表服务", description = "路由区暂存日志表")
public class RcBdcRouteHoldLogController extends BaseController<RcBdcRouteHoldLogEntity> {

    private final IRcBdcRouteHoldLogService rcBdcRouteHoldLogService;

    @Autowired
    public RcBdcRouteHoldLogController(IRcBdcRouteHoldLogService rcBdcRouteHoldLogService) {
        this.crudService = rcBdcRouteHoldLogService;
        this.rcBdcRouteHoldLogService = rcBdcRouteHoldLogService;
    }

}