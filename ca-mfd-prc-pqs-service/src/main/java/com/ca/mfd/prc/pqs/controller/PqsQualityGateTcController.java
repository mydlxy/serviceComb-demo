package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateTcEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateTcService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: QG检查项-车型Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqualitygatetc")
@Tag(name = "QG检查项-车型服务", description = "QG检查项-车型")
public class PqsQualityGateTcController extends BaseController<PqsQualityGateTcEntity> {

    private final IPqsQualityGateTcService pqsQualityGateTcService;

    @Autowired
    public PqsQualityGateTcController(IPqsQualityGateTcService pqsQualityGateTcService) {
        this.crudService = pqsQualityGateTcService;
        this.pqsQualityGateTcService = pqsQualityGateTcService;
    }

}