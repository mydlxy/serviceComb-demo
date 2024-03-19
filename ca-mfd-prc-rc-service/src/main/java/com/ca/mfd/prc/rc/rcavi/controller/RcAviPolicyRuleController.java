package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviPolicyRuleEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviPolicyRuleService;
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
@RequestMapping("rcavipolicyrule")
@Tag(name = "路由策略规则明细服务", description = "路由策略规则明细")
public class RcAviPolicyRuleController extends BaseController<RcAviPolicyRuleEntity> {
    private final IRcAviPolicyRuleService rcPolicyRuleService;

    @Autowired
    public RcAviPolicyRuleController(IRcAviPolicyRuleService rcPolicyRuleService) {
        this.crudService = rcPolicyRuleService;
        this.rcPolicyRuleService = rcPolicyRuleService;
    }

}