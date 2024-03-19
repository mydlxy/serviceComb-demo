package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.AuditQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikWorkstationService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AUDIT百格图关联的岗位Controller
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@RestController
@RequestMapping("auditqualitymatrikworkstation")
@Tag(name = "AUDIT百格图关联的岗位服务", description = "AUDIT百格图关联的岗位")
public class AuditQualityMatrikWorkstationController extends BaseController<AuditQualityMatrikWorkstationEntity> {

    private IAuditQualityMatrikWorkstationService auditQualityMatrikWorkstationService;

    @Autowired
    public AuditQualityMatrikWorkstationController(IAuditQualityMatrikWorkstationService auditQualityMatrikWorkstationService) {
        this.crudService = auditQualityMatrikWorkstationService;
        this.auditQualityMatrikWorkstationService = auditQualityMatrikWorkstationService;
    }

}