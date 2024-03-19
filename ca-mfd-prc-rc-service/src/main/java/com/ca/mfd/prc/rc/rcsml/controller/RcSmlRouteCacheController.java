package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRouteCacheEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRouteCacheService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由缓存Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutecache")
@Tag(name = "路由缓存服务", description = "路由缓存")
public class RcSmlRouteCacheController extends BaseController<RcSmlRouteCacheEntity> {

    private final IRcSmlRouteCacheService rcSmlRouteCacheService;

    @Autowired
    public RcSmlRouteCacheController(IRcSmlRouteCacheService rcSmlRouteCacheService) {
        this.crudService = rcSmlRouteCacheService;
        this.rcSmlRouteCacheService = rcSmlRouteCacheService;
    }

}