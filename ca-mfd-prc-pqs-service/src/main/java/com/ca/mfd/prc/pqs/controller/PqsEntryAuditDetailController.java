package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsEntryAuditDetailEntity;
import com.ca.mfd.prc.pqs.service.IPqsEntryAuditDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 评审工单明细Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsentryauditdetail")
@Tag(name = "评审工单明细服务", description = "评审工单明细")
public class PqsEntryAuditDetailController extends BaseController<PqsEntryAuditDetailEntity> {

    private final IPqsEntryAuditDetailService pqsEntryAuditDetailService;

    @Autowired
    public PqsEntryAuditDetailController(IPqsEntryAuditDetailService pqsEntryAuditDetailService) {
        this.crudService = pqsEntryAuditDetailService;
        this.pqsEntryAuditDetailService = pqsEntryAuditDetailService;
    }

}