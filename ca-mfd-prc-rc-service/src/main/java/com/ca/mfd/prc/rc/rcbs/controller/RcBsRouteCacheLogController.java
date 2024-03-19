package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteCacheLogEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteCacheLogService;
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
@RequestMapping("rcbsroutecachelog")
@Tag(name = "路由出车记录服务", description = "路由出车记录")
public class RcBsRouteCacheLogController extends BaseController<RcBsRouteCacheLogEntity> {

    private final IRcBsRouteCacheLogService rcRouteCacheLogService;

    @Autowired
    public RcBsRouteCacheLogController(IRcBsRouteCacheLogService rcRouteCacheLogService) {
        this.crudService = rcRouteCacheLogService;
        this.rcRouteCacheLogService = rcRouteCacheLogService;
    }

}