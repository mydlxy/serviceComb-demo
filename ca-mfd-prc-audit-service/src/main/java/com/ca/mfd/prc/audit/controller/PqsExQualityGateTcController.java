package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExQualityGateTcEntity;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateTcService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺 QG检查项-车型Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexqualitygatetc")
@Tag(name = "精致工艺 QG检查项-车型服务", description = "精致工艺 QG检查项-车型")
public class PqsExQualityGateTcController extends BaseController<PqsExQualityGateTcEntity> {

    private IPqsExQualityGateTcService pqsExQualityGateTcService;

    @Autowired
    public PqsExQualityGateTcController(IPqsExQualityGateTcService pqsExQualityGateTcService) {
        this.crudService = pqsExQualityGateTcService;
        this.pqsExQualityGateTcService = pqsExQualityGateTcService;
    }

}