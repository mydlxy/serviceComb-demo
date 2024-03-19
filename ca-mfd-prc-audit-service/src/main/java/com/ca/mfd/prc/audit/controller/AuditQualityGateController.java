package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.AuditQualityGateEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityGateService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AUDIT QG检查项Controller
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@RestController
@RequestMapping("auditqualitygate")
@Tag(name = "AUDIT QG检查项服务", description = "AUDIT QG检查项")
public class AuditQualityGateController extends BaseController<AuditQualityGateEntity> {

    private IAuditQualityGateService auditQualityGateService;

    @Autowired
    public AuditQualityGateController(IAuditQualityGateService auditQualityGateService) {
        this.crudService = auditQualityGateService;
        this.auditQualityGateService = auditQualityGateService;
    }

}