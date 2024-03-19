package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskDetailEntity;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyRiskDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 质量围堵-清单Controller
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@RestController
@RequestMapping("pqsdefectanomalyriskdetail")
@Tag(name = "质量围堵-清单服务", description = "质量围堵-清单")
public class PqsDefectAnomalyRiskDetailController extends BaseController<PqsDefectAnomalyRiskDetailEntity> {

    private final IPqsDefectAnomalyRiskDetailService pqsDefectAnomalyRiskDetailService;

    @Autowired
    public PqsDefectAnomalyRiskDetailController(IPqsDefectAnomalyRiskDetailService pqsDefectAnomalyRiskDetailService) {
        this.crudService = pqsDefectAnomalyRiskDetailService;
        this.pqsDefectAnomalyRiskDetailService = pqsDefectAnomalyRiskDetailService;
    }

}