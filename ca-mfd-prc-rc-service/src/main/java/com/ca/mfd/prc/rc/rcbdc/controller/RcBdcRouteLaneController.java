package com.ca.mfd.prc.rc.rcbdc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbdc.entity.RcBdcRouteLaneEntity;
import com.ca.mfd.prc.rc.rcbdc.service.IRcBdcRouteLaneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由车道Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutelane")
@Tag(name = "路由车道服务", description = "路由车道")
public class RcBdcRouteLaneController extends BaseController<RcBdcRouteLaneEntity> {

    private final IRcBdcRouteLaneService rcBdcRouteLaneService;

    @Autowired
    public RcBdcRouteLaneController(IRcBdcRouteLaneService rcBdcRouteLaneService) {
        this.crudService = rcBdcRouteLaneService;
        this.rcBdcRouteLaneService = rcBdcRouteLaneService;
    }

}