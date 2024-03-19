package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsRinTimeConfigEntity;
import com.ca.mfd.prc.pps.service.IPpsRinTimeConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eric.zhou
 * @Description: 电池RIN码时间配置Controller
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@RestController
@RequestMapping("ppsrintimeconfig")
@Tag(name = "电池RIN码时间配置服务", description = "电池RIN码时间配置")
public class PpsRinTimeConfigController extends BaseController<PpsRinTimeConfigEntity> {

    private final IPpsRinTimeConfigService prcPpsRinTimeConfigService;

    @Autowired
    public PpsRinTimeConfigController(IPpsRinTimeConfigService prcPpsRinTimeConfigService) {
        this.crudService = prcPpsRinTimeConfigService;
        this.prcPpsRinTimeConfigService = prcPpsRinTimeConfigService;
    }

}