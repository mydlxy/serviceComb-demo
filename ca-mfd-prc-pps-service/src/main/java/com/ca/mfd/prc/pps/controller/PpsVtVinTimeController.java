package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsVtVinTimeEntity;
import com.ca.mfd.prc.pps.service.IPpsVtVinTimeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eric.zhou
 * @Description: VIN推迟时间配置Controller
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@RestController
@RequestMapping("ppsvtvintime")
@Tag(name = "VIN推迟时间配置服务", description = "VIN推迟时间配置")
public class PpsVtVinTimeController extends BaseController<PpsVtVinTimeEntity> {

    private final IPpsVtVinTimeService prcPpsVtVinTimeService;

    @Autowired
    public PpsVtVinTimeController(IPpsVtVinTimeService prcPpsVtVinTimeService) {
        this.crudService = prcPpsVtVinTimeService;
        this.prcPpsVtVinTimeService = prcPpsVtVinTimeService;
    }


}