package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsPolicyEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsPolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由策略Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcbspolicy")
@Tag(name = "路由策略服务", description = "路由策略")
public class RcBsPolicyController extends BaseController<RcBsPolicyEntity> {
    private final IRcBsPolicyService rcPolicyService;

    @Autowired
    public RcBsPolicyController(IRcBsPolicyService rcPolicyService) {
        this.crudService = rcPolicyService;
        this.rcPolicyService = rcPolicyService;
    }

}