package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRouteLaneEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRouteLaneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由车道Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutelane")
@Tag(name = "路由车道服务", description = "路由车道")
public class RcSmlRouteLaneController extends BaseController<RcSmlRouteLaneEntity> {

    private final IRcSmlRouteLaneService rcSmlRouteLaneService;

    @Autowired
    public RcSmlRouteLaneController(IRcSmlRouteLaneService rcSmlRouteLaneService) {
        this.crudService = rcSmlRouteLaneService;
        this.rcSmlRouteLaneService = rcSmlRouteLaneService;
    }

}