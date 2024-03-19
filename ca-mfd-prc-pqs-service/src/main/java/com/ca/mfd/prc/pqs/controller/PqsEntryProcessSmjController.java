package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessSmjEntity;
import com.ca.mfd.prc.pqs.service.IPqsEntryProcessSmjService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 质检工单-过程检验_首末检验Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsentryprocesssmj")
@Tag(name = "质检工单-过程检验_首末检验服务", description = "质检工单-过程检验_首末检验")
public class PqsEntryProcessSmjController extends BaseController<PqsEntryProcessSmjEntity> {

    private final IPqsEntryProcessSmjService pqsEntryProcessSmjService;

    @Autowired
    public PqsEntryProcessSmjController(IPqsEntryProcessSmjService pqsEntryProcessSmjService) {
        this.crudService = pqsEntryProcessSmjService;
        this.pqsEntryProcessSmjService = pqsEntryProcessSmjService;
    }

}