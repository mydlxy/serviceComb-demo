package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteLogEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由记录Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcaviroutelog")
@Tag(name = "路由记录服务", description = "路由记录")
public class RcAviRouteLogController extends BaseController<RcAviRouteLogEntity> {

    private final IRcAviRouteLogService rcAviRouteLogService;

    @Autowired
    public RcAviRouteLogController(IRcAviRouteLogService rcAviRouteLogService) {
        this.crudService = rcAviRouteLogService;
        this.rcAviRouteLogService = rcAviRouteLogService;
    }

}