package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRouteCacheLogEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRouteCacheLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由出车记录Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutecachelog")
@Tag(name = "路由出车记录服务", description = "路由出车记录")
public class RcSmlRouteCacheLogController extends BaseController<RcSmlRouteCacheLogEntity> {

    private final IRcSmlRouteCacheLogService rcSmlRouteCacheLogService;

    @Autowired
    public RcSmlRouteCacheLogController(IRcSmlRouteCacheLogService rcSmlRouteCacheLogService) {
        this.crudService = rcSmlRouteCacheLogService;
        this.rcSmlRouteCacheLogService = rcSmlRouteCacheLogService;
    }

}