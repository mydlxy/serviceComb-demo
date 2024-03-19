package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExQualityGateAnomalyEntity;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateAnomalyService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺 QG检验项-缺陷Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexqualitygateanomaly")
@Tag(name = "精致工艺 QG检验项-缺陷服务", description = "精致工艺 QG检验项-缺陷")
public class PqsExQualityGateAnomalyController extends BaseController<PqsExQualityGateAnomalyEntity> {

    private IPqsExQualityGateAnomalyService pqsExQualityGateAnomalyService;

    @Autowired
    public PqsExQualityGateAnomalyController(IPqsExQualityGateAnomalyService pqsExQualityGateAnomalyService) {
        this.crudService = pqsExQualityGateAnomalyService;
        this.pqsExQualityGateAnomalyService = pqsExQualityGateAnomalyService;
    }

}