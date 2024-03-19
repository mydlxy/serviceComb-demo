package com.ca.mfd.prc.rc.rcbdc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbdc.entity.RcBdcRouteCacheEntity;
import com.ca.mfd.prc.rc.rcbdc.service.IRcBdcRouteCacheService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由缓存Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutecache")
@Tag(name = "路由缓存服务", description = "路由缓存")
public class RcBdcRouteCacheController extends BaseController<RcBdcRouteCacheEntity> {

    private final IRcBdcRouteCacheService rcBdcRouteCacheService;

    @Autowired
    public RcBdcRouteCacheController(IRcBdcRouteCacheService rcBdcRouteCacheService) {
        this.crudService = rcBdcRouteCacheService;
        this.rcBdcRouteCacheService = rcBdcRouteCacheService;
    }

}