package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsEntryQyxjEntity;
import com.ca.mfd.prc.pqs.service.IPqsEntryQyxjService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 区域巡检Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsentryqyxj")
@Tag(name = "区域巡检服务", description = "区域巡检")
public class PqsEntryQyxjController extends BaseController<PqsEntryQyxjEntity> {

    private final IPqsEntryQyxjService pqsEntryQyxjService;

    @Autowired
    public PqsEntryQyxjController(IPqsEntryQyxjService pqsEntryQyxjService) {
        this.crudService = pqsEntryQyxjService;
        this.pqsEntryQyxjService = pqsEntryQyxjService;
    }

}