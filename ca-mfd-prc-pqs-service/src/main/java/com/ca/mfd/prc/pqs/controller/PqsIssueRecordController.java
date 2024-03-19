package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsIssueRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsIssueRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 问题预警记录Controller
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@RestController
@RequestMapping("pqsissuerecord")
@Tag(name = "问题预警记录服务", description = "问题预警记录")
public class PqsIssueRecordController extends BaseController<PqsIssueRecordEntity> {

    private IPqsIssueRecordService pqsIssueRecordService;

    @Autowired
    public PqsIssueRecordController(IPqsIssueRecordService pqsIssueRecordService) {
        this.crudService = pqsIssueRecordService;
        this.pqsIssueRecordService = pqsIssueRecordService;
    }

}