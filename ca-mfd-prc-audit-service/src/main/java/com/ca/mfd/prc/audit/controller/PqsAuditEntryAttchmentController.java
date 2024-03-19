package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsAuditEntryAttchmentEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditEntryAttchmentService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: AUDIT附件Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditentryattchment")
@Tag(name = "AUDIT附件服务", description = "AUDIT附件")
public class PqsAuditEntryAttchmentController extends BaseController<PqsAuditEntryAttchmentEntity> {

    private final IPqsAuditEntryAttchmentService pqsAuditEntryAttchmentService;

    @Autowired
    public PqsAuditEntryAttchmentController(IPqsAuditEntryAttchmentService pqsAuditEntryAttchmentService) {
        this.crudService = pqsAuditEntryAttchmentService;
        this.pqsAuditEntryAttchmentService = pqsAuditEntryAttchmentService;
    }

}