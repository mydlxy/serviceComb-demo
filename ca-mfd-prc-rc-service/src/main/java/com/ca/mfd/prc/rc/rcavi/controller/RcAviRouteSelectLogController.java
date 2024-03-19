package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteSelectLogEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteSelectLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点操作履历Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcavirouteselectlog")
@Tag(name = "路由选择操作履历服务", description = "路由选择操作履历")
public class RcAviRouteSelectLogController extends BaseController<RcAviRouteSelectLogEntity> {

    private final IRcAviRouteSelectLogService rcAviRouteSelectLogService;

    @Autowired
    public RcAviRouteSelectLogController(IRcAviRouteSelectLogService rcAviRouteSelectLogService) {
        this.crudService = rcAviRouteSelectLogService;
        this.rcAviRouteSelectLogService = rcAviRouteSelectLogService;
    }

}