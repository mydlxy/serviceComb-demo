package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsPolicyRuleEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsPolicyRuleService;
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
@RequestMapping("rcbspolicyrule")
@Tag(name = "路由策略规则明细服务", description = "路由策略规则明细")
public class RcBsPolicyRuleController extends BaseController<RcBsPolicyRuleEntity> {
    private final IRcBsPolicyRuleService rcPolicyRuleService;

    @Autowired
    public RcBsPolicyRuleController(IRcBsPolicyRuleService rcPolicyRuleService) {
        this.crudService = rcPolicyRuleService;
        this.rcPolicyRuleService = rcPolicyRuleService;
    }

}