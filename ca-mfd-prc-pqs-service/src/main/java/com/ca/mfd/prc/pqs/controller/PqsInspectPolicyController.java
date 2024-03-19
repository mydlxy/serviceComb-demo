package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsInspectPolicyEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectPolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 检验策略Controller
 * @author inkelink
 * @date 2023年11月02日
 * @变更说明 BY inkelink At 2023年11月02日
 */
@RestController
@RequestMapping("pqsinspectpolicy")
@Tag(name = "检验策略服务", description = "检验策略")
public class PqsInspectPolicyController extends BaseController<PqsInspectPolicyEntity> {

    private IPqsInspectPolicyService pqsInspectPolicyService;

    @Autowired
    public PqsInspectPolicyController(IPqsInspectPolicyService pqsInspectPolicyService) {
        this.crudService = pqsInspectPolicyService;
        this.pqsInspectPolicyService = pqsInspectPolicyService;
    }

}