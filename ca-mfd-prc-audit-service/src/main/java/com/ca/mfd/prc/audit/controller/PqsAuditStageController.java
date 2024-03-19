package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsAuditStageEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditStageService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: AUDIT阶段配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditstage")
@Tag(name = "AUDIT阶段配置服务", description = "AUDIT阶段配置")
public class PqsAuditStageController extends BaseController<PqsAuditStageEntity> {

    private final IPqsAuditStageService pqsAuditStageService;

    @Autowired
    public PqsAuditStageController(IPqsAuditStageService pqsAuditStageService) {
        this.crudService = pqsAuditStageService;
        this.pqsAuditStageService = pqsAuditStageService;
    }

}