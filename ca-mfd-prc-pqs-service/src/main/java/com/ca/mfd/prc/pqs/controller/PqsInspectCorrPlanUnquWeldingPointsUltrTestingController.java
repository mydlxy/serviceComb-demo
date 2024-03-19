package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectCorrPlanUnquWeldingPointsUltrTestingEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectCorrPlanUnquWeldingPointsUltrTestingService;
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
 * @Description: 超声波检测不合格焊点纠正计划Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectcorrplanunquweldingpointsultrtesting")
@Tag(name = "超声波检测不合格焊点纠正计划服务", description = "超声波检测不合格焊点纠正计划")
public class PqsInspectCorrPlanUnquWeldingPointsUltrTestingController extends BaseWithDefValController<PqsInspectCorrPlanUnquWeldingPointsUltrTestingEntity> {

    private IPqsInspectCorrPlanUnquWeldingPointsUltrTestingService pqsInspectCorrPlanUnquWeldingPointsUltrTestingService;

    @Autowired
    public PqsInspectCorrPlanUnquWeldingPointsUltrTestingController(IPqsInspectCorrPlanUnquWeldingPointsUltrTestingService pqsInspectCorrPlanUnquWeldingPointsUltrTestingService) {
        this.crudService = pqsInspectCorrPlanUnquWeldingPointsUltrTestingService;
        this.pqsInspectCorrPlanUnquWeldingPointsUltrTestingService = pqsInspectCorrPlanUnquWeldingPointsUltrTestingService;
    }

}