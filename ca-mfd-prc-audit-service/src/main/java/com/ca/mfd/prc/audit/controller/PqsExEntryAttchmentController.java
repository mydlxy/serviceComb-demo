package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExEntryAttchmentEntity;
import com.ca.mfd.prc.audit.service.IPqsExEntryAttchmentService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺附件Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexentryattchment")
@Tag(name = "精致工艺附件服务", description = "精致工艺附件")
public class PqsExEntryAttchmentController extends BaseController<PqsExEntryAttchmentEntity> {

    private IPqsExEntryAttchmentService pqsExEntryAttchmentService;

    @Autowired
    public PqsExEntryAttchmentController(IPqsExEntryAttchmentService pqsExEntryAttchmentService) {
        this.crudService = pqsExEntryAttchmentService;
        this.pqsExEntryAttchmentService = pqsExEntryAttchmentService;
    }

}