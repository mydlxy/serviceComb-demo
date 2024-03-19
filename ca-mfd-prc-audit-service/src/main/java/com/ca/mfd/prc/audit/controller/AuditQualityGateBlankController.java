package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.AuditQualityGateBlankEntity;
import com.ca.mfd.prc.audit.service.IAuditQualityGateBlankService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AUDIT QG检验项-色块Controller
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@RestController
@RequestMapping("auditqualitygateblank")
@Tag(name = "AUDIT QG检验项-色块服务", description = "AUDIT QG检验项-色块")
public class AuditQualityGateBlankController extends BaseController<AuditQualityGateBlankEntity> {

    private IAuditQualityGateBlankService auditQualityGateBlankService;

    @Autowired
    public AuditQualityGateBlankController(IAuditQualityGateBlankService auditQualityGateBlankService) {
        this.crudService = auditQualityGateBlankService;
        this.auditQualityGateBlankService = auditQualityGateBlankService;
    }

}