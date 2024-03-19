package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsAuditMmTargetEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditMmTargetService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: Audit质量月目标设置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditmmtarget")
@Tag(name = "Audit质量月目标设置服务", description = "Audit质量月目标设置")
public class PqsAuditMmTargetController extends BaseController<PqsAuditMmTargetEntity> {

    private final IPqsAuditMmTargetService pqsAuditMmTargetService;

    @Autowired
    public PqsAuditMmTargetController(IPqsAuditMmTargetService pqsAuditMmTargetService) {
        this.crudService = pqsAuditMmTargetService;
        this.pqsAuditMmTargetService = pqsAuditMmTargetService;
    }

}