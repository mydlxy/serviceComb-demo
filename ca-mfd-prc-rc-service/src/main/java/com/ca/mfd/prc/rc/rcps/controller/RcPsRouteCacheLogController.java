package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteCacheLogEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteCacheLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由出车记录Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpsroutecachelog")
@Tag(name = "路由出车记录服务", description = "路由出车记录")
public class RcPsRouteCacheLogController extends BaseController<RcPsRouteCacheLogEntity> {

    private final IRcPsRouteCacheLogService rcRouteCacheLogService;

    @Autowired
    public RcPsRouteCacheLogController(IRcPsRouteCacheLogService rcRouteCacheLogService) {
        this.crudService = rcRouteCacheLogService;
        this.rcRouteCacheLogService = rcRouteCacheLogService;
    }

}