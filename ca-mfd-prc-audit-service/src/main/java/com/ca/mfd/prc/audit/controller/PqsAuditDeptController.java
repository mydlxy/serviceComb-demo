package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsAuditDeptEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditDeptService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: AUDIT责任部门配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditdept")
@Tag(name = "AUDIT责任部门配置服务", description = "AUDIT责任部门配置")
public class PqsAuditDeptController extends BaseController<PqsAuditDeptEntity> {

    private final IPqsAuditDeptService pqsAuditDeptService;

    @Autowired
    public PqsAuditDeptController(IPqsAuditDeptService pqsAuditDeptService) {
        this.crudService = pqsAuditDeptService;
        this.pqsAuditDeptService = pqsAuditDeptService;
    }

}