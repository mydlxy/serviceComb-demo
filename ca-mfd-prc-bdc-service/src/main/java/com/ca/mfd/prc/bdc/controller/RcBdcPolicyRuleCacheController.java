package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.entity.RcBdcPolicyRuleCacheEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcPolicyRuleCacheService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由策略计算缓存Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcpolicyrulecache")
@Tag(name = "路由策略计算缓存服务", description = "路由策略计算缓存")
public class RcBdcPolicyRuleCacheController extends BaseController<RcBdcPolicyRuleCacheEntity> {

    private final IRcBdcPolicyRuleCacheService rcBdcPolicyRuleCacheService;

    @Autowired
    public RcBdcPolicyRuleCacheController(IRcBdcPolicyRuleCacheService rcBdcPolicyRuleCacheService) {
        this.crudService = rcBdcPolicyRuleCacheService;
        this.rcBdcPolicyRuleCacheService = rcBdcPolicyRuleCacheService;
    }

}