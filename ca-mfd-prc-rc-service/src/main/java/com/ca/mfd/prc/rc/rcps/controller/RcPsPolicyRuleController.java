package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsPolicyRuleEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsPolicyRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由策略规则明细Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpspolicyrule")
@Tag(name = "路由策略规则明细服务", description = "路由策略规则明细")
public class RcPsPolicyRuleController extends BaseController<RcPsPolicyRuleEntity> {
    private final IRcPsPolicyRuleService rcPolicyRuleService;

    @Autowired
    public RcPsPolicyRuleController(IRcPsPolicyRuleService rcPolicyRuleService) {
        this.crudService = rcPolicyRuleService;
        this.rcPolicyRuleService = rcPolicyRuleService;
    }

}