package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikEntity;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺百格图Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexqualitymatrik")
@Tag(name = "精致工艺百格图服务", description = "精致工艺百格图")
public class PqsExQualityMatrikController extends BaseController<PqsExQualityMatrikEntity> {

    private IPqsExQualityMatrikService pqsExQualityMatrikService;

    @Autowired
    public PqsExQualityMatrikController(IPqsExQualityMatrikService pqsExQualityMatrikService) {
        this.crudService = pqsExQualityMatrikService;
        this.pqsExQualityMatrikService = pqsExQualityMatrikService;
    }

}