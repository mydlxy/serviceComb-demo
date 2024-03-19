package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.AuditQualityGateTcEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityGateTcService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AUDIT QG检查项-车型Controller
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@RestController
@RequestMapping("auditqualitygatetc")
@Tag(name = "AUDIT QG检查项-车型服务", description = "AUDIT QG检查项-车型")
public class AuditQualityGateTcController extends BaseController<AuditQualityGateTcEntity> {

    private IAuditQualityGateTcService auditQualityGateTcService;

    @Autowired
    public AuditQualityGateTcController(IAuditQualityGateTcService auditQualityGateTcService) {
        this.crudService = auditQualityGateTcService;
        this.auditQualityGateTcService = auditQualityGateTcService;
    }

}