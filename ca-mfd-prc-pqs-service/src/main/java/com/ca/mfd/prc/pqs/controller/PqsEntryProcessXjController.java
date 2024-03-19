package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessXjEntity;
import com.ca.mfd.prc.pqs.service.IPqsEntryProcessXjService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 过程检验-巡检明细Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsentryprocessxj")
@Tag(name = "过程检验-巡检明细服务", description = "过程检验-巡检明细")
public class PqsEntryProcessXjController extends BaseController<PqsEntryProcessXjEntity> {

    private final IPqsEntryProcessXjService pqsEntryProcessXjService;

    @Autowired
    public PqsEntryProcessXjController(IPqsEntryProcessXjService pqsEntryProcessXjService) {
        this.crudService = pqsEntryProcessXjService;
        this.pqsEntryProcessXjService = pqsEntryProcessXjService;
    }

}