package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.AuditQualityMatrikEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AUDIT百格图Controller
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@RestController
@RequestMapping("auditqualitymatrik")
@Tag(name = "AUDIT百格图服务", description = "AUDIT百格图")
public class AuditQualityMatrikController extends BaseController<AuditQualityMatrikEntity> {

    private IAuditQualityMatrikService auditQualityMatrikService;

    @Autowired
    public AuditQualityMatrikController(IAuditQualityMatrikService auditQualityMatrikService) {
        this.crudService = auditQualityMatrikService;
        this.auditQualityMatrikService = auditQualityMatrikService;
    }

}