package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.pm.entity.PmControlPlanEntity;
import com.ca.mfd.prc.pm.service.IPmControlPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @Description: 控制计划Controller
 * @author inkelink
 * @date 2023年11月22日
 * @变更说明 BY inkelink At 2023年11月22日
 */
@RestController
@RequestMapping("pmcontrolplan")
@Tag(name = "控制计划服务", description = "控制计划")
public class PmControlPlanController extends PmBaseController<PmControlPlanEntity> {

    private IPmControlPlanService pmControlPlanService;

    @Autowired
    public PmControlPlanController(IPmControlPlanService pmControlPlanService) {
        this.crudService = pmControlPlanService;
        this.pmControlPlanService = pmControlPlanService;
    }

}