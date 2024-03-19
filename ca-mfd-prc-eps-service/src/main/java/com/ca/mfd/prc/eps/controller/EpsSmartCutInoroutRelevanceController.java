package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsSmartCutInoroutRelevanceEntity;
import com.ca.mfd.prc.eps.service.IEpsSmartCutInoroutRelevanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: PACK切入或切出关联Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epssmartcutinoroutrelevance")
@Tag(name = "PACK切入或切出关联服务", description = "PACK切入或切出关联")
public class EpsSmartCutInoroutRelevanceController extends BaseController<EpsSmartCutInoroutRelevanceEntity> {

    private final IEpsSmartCutInoroutRelevanceService epsSmartCutInoroutRelevanceService;

    @Autowired
    public EpsSmartCutInoroutRelevanceController(IEpsSmartCutInoroutRelevanceService epsSmartCutInoroutRelevanceService) {
        this.crudService = epsSmartCutInoroutRelevanceService;
        this.epsSmartCutInoroutRelevanceService = epsSmartCutInoroutRelevanceService;
    }

}