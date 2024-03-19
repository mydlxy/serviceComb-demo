package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateWorkstationEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateWorkstationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: QG检查项关联的岗位Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqualitygateworkstation")
@Tag(name = "QG检查项关联的岗位服务", description = "QG检查项关联的岗位")
public class PqsQualityGateWorkstationController extends BaseController<PqsQualityGateWorkstationEntity> {

    private final IPqsQualityGateWorkstationService pqsQualityGateWorkstationService;

    @Autowired
    public PqsQualityGateWorkstationController(IPqsQualityGateWorkstationService pqsQualityGateWorkstationService) {
        this.crudService = pqsQualityGateWorkstationService;
        this.pqsQualityGateWorkstationService = pqsQualityGateWorkstationService;
    }

}