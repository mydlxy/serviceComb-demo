package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExMmTargetEntity;
import com.ca.mfd.prc.audit.service.IPqsExMmTargetService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺质量月目标设置Controller
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
@RestController
@RequestMapping("pqsexmmtarget")
@Tag(name = "精致工艺质量月目标设置服务", description = "精致工艺质量月目标设置")
public class PqsExMmTargetController extends BaseController<PqsExMmTargetEntity> {

    private IPqsExMmTargetService pqsExMmTargetService;

    @Autowired
    public PqsExMmTargetController(IPqsExMmTargetService pqsExMmTargetService) {
        this.crudService = pqsExMmTargetService;
        this.pqsExMmTargetService = pqsExMmTargetService;
    }

}