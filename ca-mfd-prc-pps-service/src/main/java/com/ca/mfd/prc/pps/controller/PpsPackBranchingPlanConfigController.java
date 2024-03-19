package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsPackBranchingPlanConfigEntity;
import com.ca.mfd.prc.pps.service.IPpsPackBranchingPlanConfigService;
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
 * @Description: 电池分线计划配置Controller
 * @author inkelink
 * @date 2024年01月22日
 * @变更说明 BY inkelink At 2024年01月22日
 */
@RestController
@RequestMapping("ppspackbranchingplanconfig")
@Tag(name = "电池分线计划配置服务", description = "电池分线计划配置")
public class PpsPackBranchingPlanConfigController extends BaseController<PpsPackBranchingPlanConfigEntity> {

    private IPpsPackBranchingPlanConfigService ppsPackBranchingPlanConfigService;

    @Autowired
    public PpsPackBranchingPlanConfigController(IPpsPackBranchingPlanConfigService ppsPackBranchingPlanConfigService) {
        this.crudService = ppsPackBranchingPlanConfigService;
        this.ppsPackBranchingPlanConfigService = ppsPackBranchingPlanConfigService;
    }

}