package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectBlockPolicyEntity;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectBlockPolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 缺陷堵塞策略Controller
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
@RestController
@RequestMapping("pqsproductdefectblockpolicy")
@Tag(name = "缺陷堵塞策略服务", description = "缺陷堵塞策略")
public class PqsProductDefectBlockPolicyController extends BaseController<PqsProductDefectBlockPolicyEntity> {

    private final IPqsProductDefectBlockPolicyService pqsProductDefectBlockPolicyService;

    @Autowired
    public PqsProductDefectBlockPolicyController(IPqsProductDefectBlockPolicyService pqsProductDefectBlockPolicyService) {
        this.crudService = pqsProductDefectBlockPolicyService;
        this.pqsProductDefectBlockPolicyService = pqsProductDefectBlockPolicyService;
    }

}