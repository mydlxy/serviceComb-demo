package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsProductMmLockPolicyEntity;
import com.ca.mfd.prc.pqs.service.IPqsProductMmLockPolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 追溯件阻塞Controller
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
@RestController
@RequestMapping("pqsproductmmlockpolicy")
@Tag(name = "追溯件阻塞服务", description = "追溯件阻塞")
public class PqsProductMmLockPolicyController extends BaseController<PqsProductMmLockPolicyEntity> {

    private final IPqsProductMmLockPolicyService pqsProductMmLockPolicyService;

    @Autowired
    public PqsProductMmLockPolicyController(IPqsProductMmLockPolicyService pqsProductMmLockPolicyService) {
        this.crudService = pqsProductMmLockPolicyService;
        this.pqsProductMmLockPolicyService = pqsProductMmLockPolicyService;
    }

}