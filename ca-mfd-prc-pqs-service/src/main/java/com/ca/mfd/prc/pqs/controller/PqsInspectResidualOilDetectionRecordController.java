package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectResidualOilDetectionRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectResidualOilDetectionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 残油量检测记录表Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectresidualoildetectionrecord")
@Tag(name = "残油量检测记录表服务", description = "残油量检测记录表")
public class PqsInspectResidualOilDetectionRecordController extends BaseWithDefValController<PqsInspectResidualOilDetectionRecordEntity> {

    private IPqsInspectResidualOilDetectionRecordService pqsInspectResidualOilDetectionRecordService;

    @Autowired
    public PqsInspectResidualOilDetectionRecordController(IPqsInspectResidualOilDetectionRecordService pqsInspectResidualOilDetectionRecordService) {
        this.crudService = pqsInspectResidualOilDetectionRecordService;
        this.pqsInspectResidualOilDetectionRecordService = pqsInspectResidualOilDetectionRecordService;
    }

}