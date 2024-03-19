package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsRinStandardConfigEntity;
import com.ca.mfd.prc.pps.service.IPpsRinStandardConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eric.zhou
 * @Description: 电池RIN码前14位配置Controller
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@RestController
@RequestMapping("ppsrinstandardconfig")
@Tag(name = "电池RIN码前14位配置服务", description = "电池RIN码前14位配置")
public class PpsRinStandardConfigController extends BaseController<PpsRinStandardConfigEntity> {

    private final IPpsRinStandardConfigService prcPpsRinStandardConfigService;

    @Autowired
    public PpsRinStandardConfigController(IPpsRinStandardConfigService prcPpsRinStandardConfigService) {
        this.crudService = prcPpsRinStandardConfigService;
        this.prcPpsRinStandardConfigService = prcPpsRinStandardConfigService;
    }

}