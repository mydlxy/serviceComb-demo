package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectPlanTearingUnquSoldPointEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectPlanTearingUnquSoldPointService;
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
 * @Description: 撕裂不合格焊点整改计划表Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectplantearingunqusoldpoint")
@Tag(name = "撕裂不合格焊点整改计划表服务", description = "撕裂不合格焊点整改计划表")
public class PqsInspectPlanTearingUnquSoldPointController extends BaseWithDefValController<PqsInspectPlanTearingUnquSoldPointEntity> {

    private IPqsInspectPlanTearingUnquSoldPointService pqsInspectPlanTearingUnquSoldPointService;

    @Autowired
    public PqsInspectPlanTearingUnquSoldPointController(IPqsInspectPlanTearingUnquSoldPointService pqsInspectPlanTearingUnquSoldPointService) {
        this.crudService = pqsInspectPlanTearingUnquSoldPointService;
        this.pqsInspectPlanTearingUnquSoldPointService = pqsInspectPlanTearingUnquSoldPointService;
    }

}