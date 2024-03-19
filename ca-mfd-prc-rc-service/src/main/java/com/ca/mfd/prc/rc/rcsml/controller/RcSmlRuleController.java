package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRuleEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由规则Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlrule")
@Tag(name = "路由规则服务", description = "路由规则")
public class RcSmlRuleController extends BaseController<RcSmlRuleEntity> {

    private final IRcSmlRuleService rcSmlRuleService;

    @Autowired
    public RcSmlRuleController(IRcSmlRuleService rcSmlRuleService) {
        this.crudService = rcSmlRuleService;
        this.rcSmlRuleService = rcSmlRuleService;
    }

}