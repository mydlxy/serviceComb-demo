package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsPlanLockConfigEntity;
import com.ca.mfd.prc.pps.service.IPpsPlanLockConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eric.zhou
 * @Description: 生产计划锁定配置Controller
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@RestController
@RequestMapping("ppsplanlockconfig")
@Tag(name = "生产计划锁定配置服务", description = "生产计划锁定配置")
public class PpsPlanLockConfigController extends BaseController<PpsPlanLockConfigEntity> {

    private final IPpsPlanLockConfigService prcPpsPlanLockConfigService;

    @Autowired
    public PpsPlanLockConfigController(IPpsPlanLockConfigService prcPpsPlanLockConfigService) {
        this.crudService = prcPpsPlanLockConfigService;
        this.prcPpsPlanLockConfigService = prcPpsPlanLockConfigService;
    }

}