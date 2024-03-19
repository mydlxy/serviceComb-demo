package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExEntryEntity;
import com.ca.mfd.prc.audit.service.IPqsExEntryService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author inkelink
 * @Description: 精致工艺评审单Controller
 * @date 2024年01月30日
 * @变更说明 BY inkelink At 2024年01月30日
 */
@RestController
@RequestMapping("pqsexentry")
@Tag(name = "精致工艺评审单服务", description = "精致工艺评审单")
public class PqsExEntryController extends BaseController<PqsExEntryEntity> {

    private IPqsExEntryService pqsExEntryService;

    @Autowired
    public PqsExEntryController(IPqsExEntryService pqsExEntryService) {
        this.crudService = pqsExEntryService;
        this.pqsExEntryService = pqsExEntryService;
    }

}