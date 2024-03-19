package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsPartsScrapEntity;
import com.ca.mfd.prc.pqs.service.IPqsPartsScrapService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 质检工单-评审工单Controller
 * @date 2023年09月17日
 * @变更说明 BY inkelink At 2023年09月17日
 */
@RestController
@RequestMapping("pqspartsscrap")
@Tag(name = "质检工单-评审工单服务", description = "质检工单-评审工单")
public class PqsPartsScrapController extends BaseController<PqsPartsScrapEntity> {

    private final IPqsPartsScrapService pqsPartsScrapService;

    @Autowired
    public PqsPartsScrapController(IPqsPartsScrapService pqsPartsScrapService) {
        this.crudService = pqsPartsScrapService;
        this.pqsPartsScrapService = pqsPartsScrapService;
    }

}