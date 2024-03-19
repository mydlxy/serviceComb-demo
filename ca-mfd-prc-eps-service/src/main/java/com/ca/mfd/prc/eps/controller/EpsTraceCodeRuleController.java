package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsTraceCodeRuleEntity;
import com.ca.mfd.prc.eps.service.IEpsTraceCodeRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 条码追溯规则Controller
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@RestController
@RequestMapping("epstracecoderule")
@Tag(name = "条码追溯规则服务", description = "条码追溯规则")
public class EpsTraceCodeRuleController extends BaseController<EpsTraceCodeRuleEntity> {

    private final IEpsTraceCodeRuleService epsTraceCodeRuleService;

    @Autowired
    public EpsTraceCodeRuleController(IEpsTraceCodeRuleService epsTraceCodeRuleService) {
        this.crudService = epsTraceCodeRuleService;
        this.epsTraceCodeRuleService = epsTraceCodeRuleService;
    }

}