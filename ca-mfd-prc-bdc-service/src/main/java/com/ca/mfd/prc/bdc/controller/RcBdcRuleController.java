package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.entity.RcBdcRuleEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcRuleService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由规则Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcrule")
@Tag(name = "路由规则服务", description = "路由规则")
public class RcBdcRuleController extends BaseController<RcBdcRuleEntity> {

    private final IRcBdcRuleService rcBdcRuleService;

    @Autowired
    public RcBdcRuleController(IRcBdcRuleService rcBdcRuleService) {
        this.crudService = rcBdcRuleService;
        this.rcBdcRuleService = rcBdcRuleService;
    }

}