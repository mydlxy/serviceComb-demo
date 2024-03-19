package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.entity.RcBdcRouteCacheLogEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteCacheLogService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由出车记录Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutecachelog")
@Tag(name = "路由出车记录服务", description = "路由出车记录")
public class RcBdcRouteCacheLogController extends BaseController<RcBdcRouteCacheLogEntity> {

    private final IRcBdcRouteCacheLogService rcBdcRouteCacheLogService;

    @Autowired
    public RcBdcRouteCacheLogController(IRcBdcRouteCacheLogService rcBdcRouteCacheLogService) {
        this.crudService = rcBdcRouteCacheLogService;
        this.rcBdcRouteCacheLogService = rcBdcRouteCacheLogService;
    }

}