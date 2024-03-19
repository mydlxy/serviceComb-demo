package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.AuditQualityGateAnomalyEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityGateAnomalyService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AUDIT QG检验项-缺陷Controller
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@RestController
@RequestMapping("auditqualitygateanomaly")
@Tag(name = "AUDIT QG检验项-缺陷服务", description = "AUDIT QG检验项-缺陷")
public class AuditQualityGateAnomalyController extends BaseController<AuditQualityGateAnomalyEntity> {

    private IAuditQualityGateAnomalyService auditQualityGateAnomalyService;

    @Autowired
    public AuditQualityGateAnomalyController(IAuditQualityGateAnomalyService auditQualityGateAnomalyService) {
        this.crudService = auditQualityGateAnomalyService;
        this.auditQualityGateAnomalyService = auditQualityGateAnomalyService;
    }

}