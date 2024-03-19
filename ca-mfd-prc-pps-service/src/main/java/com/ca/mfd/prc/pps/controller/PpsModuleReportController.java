package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsModuleReportEntity;
import com.ca.mfd.prc.pps.service.IPpsModuleReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 预成组报工单Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("ppsmodulereport")
@Tag(name = "预成组报工单服务", description = "预成组报工单")
public class PpsModuleReportController extends BaseController<PpsModuleReportEntity> {

    private IPpsModuleReportService ppsModuleReportService;

    @Autowired
    public PpsModuleReportController(IPpsModuleReportService ppsModuleReportService) {
        this.crudService = ppsModuleReportService;
        this.ppsModuleReportService = ppsModuleReportService;
    }

}