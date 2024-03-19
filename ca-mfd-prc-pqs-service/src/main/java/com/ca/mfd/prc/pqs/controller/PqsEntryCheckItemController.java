package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsEntryCheckItemEntity;
import com.ca.mfd.prc.pqs.service.IPqsEntryCheckItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 工单检验项Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsentrycheckitem")
@Tag(name = "工单检验项服务", description = "工单检验项")
public class PqsEntryCheckItemController extends BaseController<PqsEntryCheckItemEntity> {

    private final IPqsEntryCheckItemService pqsEntryCheckItemService;

    @Autowired
    public PqsEntryCheckItemController(IPqsEntryCheckItemService pqsEntryCheckItemService) {
        this.crudService = pqsEntryCheckItemService;
        this.pqsEntryCheckItemService = pqsEntryCheckItemService;
    }

}