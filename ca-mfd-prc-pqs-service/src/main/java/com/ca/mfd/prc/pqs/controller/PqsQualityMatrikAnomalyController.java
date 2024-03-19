package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikAnomalyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 百格图-缺陷Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqualitymatrikanomaly")
@Tag(name = "百格图-缺陷服务", description = "百格图-缺陷")
public class PqsQualityMatrikAnomalyController extends BaseController<PqsQualityMatrikAnomalyEntity> {

    private final IPqsQualityMatrikAnomalyService pqsQualityMatrikAnomalyService;

    @Autowired
    public PqsQualityMatrikAnomalyController(IPqsQualityMatrikAnomalyService pqsQualityMatrikAnomalyService) {
        this.crudService = pqsQualityMatrikAnomalyService;
        this.pqsQualityMatrikAnomalyService = pqsQualityMatrikAnomalyService;
    }

}