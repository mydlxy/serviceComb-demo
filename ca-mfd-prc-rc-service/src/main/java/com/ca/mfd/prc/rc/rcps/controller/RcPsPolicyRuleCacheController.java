package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsPolicyRuleCacheEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsPolicyRuleCacheService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由策略计算缓存Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcpspolicyrulecache")
@Tag(name = "路由策略计算缓存服务", description = "路由策略计算缓存")
public class RcPsPolicyRuleCacheController extends BaseController<RcPsPolicyRuleCacheEntity> {

    private final IRcPsPolicyRuleCacheService rcPolicyRuleCacheService;

    @Autowired
    public RcPsPolicyRuleCacheController(IRcPsPolicyRuleCacheService rcPolicyRuleCacheService) {
        this.crudService = rcPolicyRuleCacheService;
        this.rcPolicyRuleCacheService = rcPolicyRuleCacheService;
    }

}