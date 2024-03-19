package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlPolicyRuleCacheEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlPolicyRuleCacheService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由策略计算缓存Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlpolicyrulecache")
@Tag(name = "路由策略计算缓存服务", description = "路由策略计算缓存")
public class RcSmlPolicyRuleCacheController extends BaseController<RcSmlPolicyRuleCacheEntity> {

    private final IRcSmlPolicyRuleCacheService rcSmlPolicyRuleCacheService;

    @Autowired
    public RcSmlPolicyRuleCacheController(IRcSmlPolicyRuleCacheService rcSmlPolicyRuleCacheService) {
        this.crudService = rcSmlPolicyRuleCacheService;
        this.rcSmlPolicyRuleCacheService = rcSmlPolicyRuleCacheService;
    }

}