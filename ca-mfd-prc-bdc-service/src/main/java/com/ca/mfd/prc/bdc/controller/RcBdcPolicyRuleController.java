package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.entity.RcBdcPolicyRuleEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcPolicyRuleService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由策略规则明细Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcpolicyrule")
@Tag(name = "路由策略规则明细服务", description = "路由策略规则明细")
public class RcBdcPolicyRuleController extends BaseController<RcBdcPolicyRuleEntity> {

    private final IRcBdcPolicyRuleService rcBdcPolicyRuleService;

    @Autowired
    public RcBdcPolicyRuleController(IRcBdcPolicyRuleService rcBdcPolicyRuleService) {
        this.crudService = rcBdcPolicyRuleService;
        this.rcBdcPolicyRuleService = rcBdcPolicyRuleService;
    }

}