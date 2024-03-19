package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteHoldLogEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteHoldLogService;
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
@RequestMapping("rcavirouteholdlog")
@Tag(name = "路由区暂存日志表服务", description = "路由区暂存日志表")
public class RcAviRouteHoldLogController extends BaseController<RcAviRouteHoldLogEntity> {

    private final IRcAviRouteHoldLogService rcRouteHoldLogService;

    @Autowired
    public RcAviRouteHoldLogController(IRcAviRouteHoldLogService rcRouteHoldLogService) {
        this.crudService = rcRouteHoldLogService;
        this.rcRouteHoldLogService = rcRouteHoldLogService;
    }

}