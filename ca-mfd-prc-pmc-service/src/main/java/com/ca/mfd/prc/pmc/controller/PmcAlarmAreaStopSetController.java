package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pmc.entity.PmcAlarmAreaStopSetEntity;
import com.ca.mfd.prc.pmc.service.IPmcAlarmAreaStopSetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 区域多设备配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("pmcalarmareastopset")
@Tag(name = "区域多设备配置")
public class PmcAlarmAreaStopSetController extends BaseController<PmcAlarmAreaStopSetEntity> {

    private final IPmcAlarmAreaStopSetService pmcAlarmAreaStopSetService;

    @Autowired
    public PmcAlarmAreaStopSetController(IPmcAlarmAreaStopSetService pmcAlarmAreaStopSetService) {
        this.crudService = pmcAlarmAreaStopSetService;
        this.pmcAlarmAreaStopSetService = pmcAlarmAreaStopSetService;
    }

}