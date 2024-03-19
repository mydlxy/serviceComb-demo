package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteLaneEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteLaneService;
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
@RequestMapping("rcbsroutelane")
@Tag(name = "路由车道服务", description = "路由车道")
public class RcBsRouteLaneController extends BaseController<RcBsRouteLaneEntity> {

    private final IRcBsRouteLaneService rcRouteLaneService;

    @Autowired
    public RcBsRouteLaneController(IRcBsRouteLaneService rcRouteLaneService) {
        this.crudService = rcRouteLaneService;
        this.rcRouteLaneService = rcRouteLaneService;
    }

}