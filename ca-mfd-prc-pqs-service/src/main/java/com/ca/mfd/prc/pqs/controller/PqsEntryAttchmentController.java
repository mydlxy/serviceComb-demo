package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsEntryAttchmentEntity;
import com.ca.mfd.prc.pqs.service.IPqsEntryAttchmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 质检附件Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsentryattchment")
@Tag(name = "质检附件服务", description = "质检附件")
public class PqsEntryAttchmentController extends BaseController<PqsEntryAttchmentEntity> {

    private final IPqsEntryAttchmentService pqsEntryAttchmentService;

    @Autowired
    public PqsEntryAttchmentController(IPqsEntryAttchmentService pqsEntryAttchmentService) {
        this.crudService = pqsEntryAttchmentService;
        this.pqsEntryAttchmentService = pqsEntryAttchmentService;
    }

}