package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsAuditWkTargetEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditWkTargetService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: Audit质量周目标设置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditwktarget")
@Tag(name = "Audit质量周目标设置服务", description = "Audit质量周目标设置")
public class PqsAuditWkTargetController extends BaseController<PqsAuditWkTargetEntity> {

    private final IPqsAuditWkTargetService pqsAuditWkTargetService;

    @Autowired
    public PqsAuditWkTargetController(IPqsAuditWkTargetService pqsAuditWkTargetService) {
        this.crudService = pqsAuditWkTargetService;
        this.pqsAuditWkTargetService = pqsAuditWkTargetService;
    }

}