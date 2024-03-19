package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsIssuePolicyEntity;
import com.ca.mfd.prc.pqs.service.IPqsIssuePolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 问题预警配置Controller
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@RestController
@RequestMapping("pqsissuepolicy")
@Tag(name = "问题预警配置服务", description = "问题预警配置")
public class PqsIssuePolicyController extends BaseController<PqsIssuePolicyEntity> {

    private IPqsIssuePolicyService pqsIssuePolicyService;

    @Autowired
    public PqsIssuePolicyController(IPqsIssuePolicyService pqsIssuePolicyService) {
        this.crudService = pqsIssuePolicyService;
        this.pqsIssuePolicyService = pqsIssuePolicyService;
    }

}