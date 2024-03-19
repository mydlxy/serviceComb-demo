package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsPlanAviEntity;
import com.ca.mfd.prc.pps.service.IPpsPlanAviService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eric.zhou
 * @Description: 计划履历;Controller
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@RestController
@RequestMapping("ppsplanavi")
@Tag(name = "计划履历服务", description = "计划履历")
public class PpsPlanAviController extends BaseController<PpsPlanAviEntity> {

    private final IPpsPlanAviService prcPpsPlanAviService;

    @Autowired
    public PpsPlanAviController(IPpsPlanAviService prcPpsPlanAviService) {
        this.crudService = prcPpsPlanAviService;
        this.prcPpsPlanAviService = prcPpsPlanAviService;
    }

}