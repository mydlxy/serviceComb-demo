package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExQualityGateWorkstationEntity;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateWorkstationService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺 QG检查项关联的岗位Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexqualitygateworkstation")
@Tag(name = "精致工艺 QG检查项关联的岗位服务", description = "精致工艺 QG检查项关联的岗位")
public class PqsExQualityGateWorkstationController extends BaseController<PqsExQualityGateWorkstationEntity> {

    private IPqsExQualityGateWorkstationService pqsExQualityGateWorkstationService;

    @Autowired
    public PqsExQualityGateWorkstationController(IPqsExQualityGateWorkstationService pqsExQualityGateWorkstationService) {
        this.crudService = pqsExQualityGateWorkstationService;
        this.pqsExQualityGateWorkstationService = pqsExQualityGateWorkstationService;
    }

}