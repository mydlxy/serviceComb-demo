package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.AuditQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikAnomalyService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AUDIT百格图-缺陷Controller
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@RestController
@RequestMapping("auditqualitymatrikanomaly")
@Tag(name = "AUDIT百格图-缺陷服务", description = "AUDIT百格图-缺陷")
public class AuditQualityMatrikAnomalyController extends BaseController<AuditQualityMatrikAnomalyEntity> {

    private IAuditQualityMatrikAnomalyService auditQualityMatrikAnomalyService;

    @Autowired
    public AuditQualityMatrikAnomalyController(IAuditQualityMatrikAnomalyService auditQualityMatrikAnomalyService) {
        this.crudService = auditQualityMatrikAnomalyService;
        this.auditQualityMatrikAnomalyService = auditQualityMatrikAnomalyService;
    }

}