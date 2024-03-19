package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteLaneEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteLaneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由车道Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpsroutelane")
@Tag(name = "路由车道服务", description = "路由车道")
public class RcPsRouteLaneController extends BaseController<RcPsRouteLaneEntity> {

    private final IRcPsRouteLaneService rcRouteLaneService;

    @Autowired
    public RcPsRouteLaneController(IRcPsRouteLaneService rcRouteLaneService) {
        this.crudService = rcRouteLaneService;
        this.rcRouteLaneService = rcRouteLaneService;
    }

}