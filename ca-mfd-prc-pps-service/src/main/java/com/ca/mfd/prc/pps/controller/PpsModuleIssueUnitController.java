package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueUnitEntity;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueUnitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 电池预成组下发小单元Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("ppsmoduleissueunit")
@Tag(name = "电池预成组下发小单元服务", description = "电池预成组下发小单元")
public class PpsModuleIssueUnitController extends BaseController<PpsModuleIssueUnitEntity> {

    private IPpsModuleIssueUnitService ppsModuleIssueUnitService;

    @Autowired
    public PpsModuleIssueUnitController(IPpsModuleIssueUnitService ppsModuleIssueUnitService) {
        this.crudService = ppsModuleIssueUnitService;
        this.ppsModuleIssueUnitService = ppsModuleIssueUnitService;
    }

}