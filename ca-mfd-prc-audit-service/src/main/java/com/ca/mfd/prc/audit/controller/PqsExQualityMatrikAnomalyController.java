package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikAnomalyService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author inkelink
 * @Description: 精致工艺百格图-缺陷Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexqualitymatrikanomaly")
@Tag(name = "精致工艺百格图-缺陷服务", description = "精致工艺百格图-缺陷")
public class PqsExQualityMatrikAnomalyController extends BaseController<PqsExQualityMatrikAnomalyEntity> {

    private IPqsExQualityMatrikAnomalyService pqsExQualityMatrikAnomalyService;

    @Autowired
    public PqsExQualityMatrikAnomalyController(IPqsExQualityMatrikAnomalyService pqsExQualityMatrikAnomalyService) {
        this.crudService = pqsExQualityMatrikAnomalyService;
        this.pqsExQualityMatrikAnomalyService = pqsExQualityMatrikAnomalyService;
    }

}