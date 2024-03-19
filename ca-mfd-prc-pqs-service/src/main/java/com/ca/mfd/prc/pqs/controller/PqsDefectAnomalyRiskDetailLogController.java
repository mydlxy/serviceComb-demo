package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskDetailLogEntity;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyRiskDetailLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 质量围堵-操作日志Controller
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@RestController
@RequestMapping("pqsdefectanomalyriskdetaillog")
@Tag(name = "质量围堵-操作日志服务", description = "质量围堵-操作日志")
public class PqsDefectAnomalyRiskDetailLogController extends BaseController<PqsDefectAnomalyRiskDetailLogEntity> {

    private final IPqsDefectAnomalyRiskDetailLogService pqsDefectAnomalyRiskDetailLogService;

    @Autowired
    public PqsDefectAnomalyRiskDetailLogController(IPqsDefectAnomalyRiskDetailLogService pqsDefectAnomalyRiskDetailLogService) {
        this.crudService = pqsDefectAnomalyRiskDetailLogService;
        this.pqsDefectAnomalyRiskDetailLogService = pqsDefectAnomalyRiskDetailLogService;
    }

}