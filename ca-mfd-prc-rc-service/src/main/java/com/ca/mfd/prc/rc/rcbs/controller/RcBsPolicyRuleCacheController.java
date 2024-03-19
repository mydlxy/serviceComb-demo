package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsPolicyRuleCacheEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsPolicyRuleCacheService;
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
@RequestMapping("rcbspolicyrulecache")
@Tag(name = "路由策略计算缓存服务", description = "路由策略计算缓存")
public class RcBsPolicyRuleCacheController extends BaseController<RcBsPolicyRuleCacheEntity> {

    private final IRcBsPolicyRuleCacheService rcPolicyRuleCacheService;

    @Autowired
    public RcBsPolicyRuleCacheController(IRcBsPolicyRuleCacheService rcPolicyRuleCacheService) {
        this.crudService = rcPolicyRuleCacheService;
        this.rcPolicyRuleCacheService = rcPolicyRuleCacheService;
    }

}