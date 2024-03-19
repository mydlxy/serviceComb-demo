package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikTcEntity;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikTcService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺百格图-车型Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexqualitymatriktc")
@Tag(name = "精致工艺百格图-车型服务", description = "精致工艺百格图-车型")
public class PqsExQualityMatrikTcController extends BaseController<PqsExQualityMatrikTcEntity> {

    private IPqsExQualityMatrikTcService pqsExQualityMatrikTcService;

    @Autowired
    public PqsExQualityMatrikTcController(IPqsExQualityMatrikTcService pqsExQualityMatrikTcService) {
        this.crudService = pqsExQualityMatrikTcService;
        this.pqsExQualityMatrikTcService = pqsExQualityMatrikTcService;
    }

}