package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRuleEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRuleService;
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
@RequestMapping("rcavirule")
@Tag(name = "路由规则服务", description = "路由规则")
public class RcAviRuleController extends BaseController<RcAviRuleEntity> {

    private final IRcAviRuleService rcRuleService;

    @Autowired
    public RcAviRuleController(IRcAviRuleService rcRuleService) {
        this.crudService = rcRuleService;
        this.rcRuleService = rcRuleService;
    }

}