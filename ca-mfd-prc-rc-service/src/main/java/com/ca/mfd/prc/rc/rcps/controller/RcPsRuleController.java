package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRuleEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由规则Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpsrule")
@Tag(name = "路由规则服务", description = "路由规则")
public class RcPsRuleController extends BaseController<RcPsRuleEntity> {

    private final IRcPsRuleService rcRuleService;

    @Autowired
    public RcPsRuleController(IRcPsRuleService rcRuleService) {
        this.crudService = rcRuleService;
        this.rcRuleService = rcRuleService;
    }

}