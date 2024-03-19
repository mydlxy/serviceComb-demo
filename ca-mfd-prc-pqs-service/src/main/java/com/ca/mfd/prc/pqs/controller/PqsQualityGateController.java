package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: QG检查项Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqualitygate")
@Tag(name = "QG检查项服务", description = "QG检查项")
public class PqsQualityGateController extends BaseController<PqsQualityGateEntity> {

    private final IPqsQualityGateService pqsQualityGateService;

    @Autowired
    public PqsQualityGateController(IPqsQualityGateService pqsQualityGateService) {
        this.crudService = pqsQualityGateService;
        this.pqsQualityGateService = pqsQualityGateService;
    }

}