package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmReactionPlanEntity;
import com.ca.mfd.prc.pm.service.IPmReactionPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @Description: 反应计划Controller
 * @author inkelink
 * @date 2023年12月29日
 * @变更说明 BY inkelink At 2023年12月29日
 */
@RestController
@RequestMapping("pmreactionplan")
@Tag(name = "反应计划服务", description = "反应计划")
public class PmReactionPlanController extends BaseController<PmReactionPlanEntity> {

    private IPmReactionPlanService pmReactionPlanService;

    @Autowired
    public PmReactionPlanController(IPmReactionPlanService pmReactionPlanService) {
        this.crudService = pmReactionPlanService;
        this.pmReactionPlanService = pmReactionPlanService;
    }

}