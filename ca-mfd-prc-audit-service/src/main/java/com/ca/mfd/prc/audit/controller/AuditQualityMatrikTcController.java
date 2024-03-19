package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.AuditQualityMatrikTcEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikTcService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AUDIT百格图-车型Controller
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@RestController
@RequestMapping("auditqualitymatriktc")
@Tag(name = "AUDIT百格图-车型服务", description = "AUDIT百格图-车型")
public class AuditQualityMatrikTcController extends BaseController<AuditQualityMatrikTcEntity> {

    private IAuditQualityMatrikTcService auditQualityMatrikTcService;

    @Autowired
    public AuditQualityMatrikTcController(IAuditQualityMatrikTcService auditQualityMatrikTcService) {
        this.crudService = auditQualityMatrikTcService;
        this.auditQualityMatrikTcService = auditQualityMatrikTcService;
    }

}