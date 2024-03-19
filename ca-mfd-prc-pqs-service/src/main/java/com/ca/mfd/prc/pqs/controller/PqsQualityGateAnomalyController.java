package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateAnomalyEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateAnomalyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: QG检验项-缺陷Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqualitygateanomaly")
@Tag(name = "QG检验项-缺陷服务", description = "QG检验项-缺陷")
public class PqsQualityGateAnomalyController extends BaseController<PqsQualityGateAnomalyEntity> {

    private final IPqsQualityGateAnomalyService pqsQualityGateAnomalyService;

    @Autowired
    public PqsQualityGateAnomalyController(IPqsQualityGateAnomalyService pqsQualityGateAnomalyService) {
        this.crudService = pqsQualityGateAnomalyService;
        this.pqsQualityGateAnomalyService = pqsQualityGateAnomalyService;
    }

}