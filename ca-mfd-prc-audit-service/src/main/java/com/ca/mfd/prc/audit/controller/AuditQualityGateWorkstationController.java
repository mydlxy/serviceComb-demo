package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.AuditQualityGateWorkstationEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityGateWorkstationService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AUDIT QG检查项关联的岗位Controller
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@RestController
@RequestMapping("auditqualitygateworkstation")
@Tag(name = "AUDIT QG检查项关联的岗位服务", description = "AUDIT QG检查项关联的岗位")
public class AuditQualityGateWorkstationController extends BaseController<AuditQualityGateWorkstationEntity> {

    private IAuditQualityGateWorkstationService auditQualityGateWorkstationService;

    @Autowired
    public AuditQualityGateWorkstationController(IAuditQualityGateWorkstationService auditQualityGateWorkstationService) {
        this.crudService = auditQualityGateWorkstationService;
        this.auditQualityGateWorkstationService = auditQualityGateWorkstationService;
    }

}