package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlPolicyRuleEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlPolicyRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由策略规则明细Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlpolicyrule")
@Tag(name = "路由策略规则明细服务", description = "路由策略规则明细")
public class RcSmlPolicyRuleController extends BaseController<RcSmlPolicyRuleEntity> {

    private final IRcSmlPolicyRuleService rcSmlPolicyRuleService;

    @Autowired
    public RcSmlPolicyRuleController(IRcSmlPolicyRuleService rcSmlPolicyRuleService) {
        this.crudService = rcSmlPolicyRuleService;
        this.rcSmlPolicyRuleService = rcSmlPolicyRuleService;
    }

}