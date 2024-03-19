package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExQualityGateEntity;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺 QG检查项Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexqualitygate")
@Tag(name = "精致工艺 QG检查项服务", description = "精致工艺 QG检查项")
public class PqsExQualityGateController extends BaseController<PqsExQualityGateEntity> {

    private IPqsExQualityGateService pqsExQualityGateService;

    @Autowired
    public PqsExQualityGateController(IPqsExQualityGateService pqsExQualityGateService) {
        this.crudService = pqsExQualityGateService;
        this.pqsExQualityGateService = pqsExQualityGateService;
    }

}