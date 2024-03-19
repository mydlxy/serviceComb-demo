package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRouteHoldLogEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRouteHoldLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区暂存日志表Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlrouteholdlog")
@Tag(name = "路由区暂存日志表服务", description = "路由区暂存日志表")
public class RcSmlRouteHoldLogController extends BaseController<RcSmlRouteHoldLogEntity> {

    private final IRcSmlRouteHoldLogService rcSmlRouteHoldLogService;

    @Autowired
    public RcSmlRouteHoldLogController(IRcSmlRouteHoldLogService rcSmlRouteHoldLogService) {
        this.crudService = rcSmlRouteHoldLogService;
        this.rcSmlRouteHoldLogService = rcSmlRouteHoldLogService;
    }

}