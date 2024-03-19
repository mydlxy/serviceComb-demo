package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQgWorkstationConfigEntity;
import com.ca.mfd.prc.pqs.service.IPqsQgWorkstationConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 质量门功能配置Controller
 * @date 2023年09月06日
 * @变更说明 BY inkelink At 2023年09月06日
 */
@RestController
@RequestMapping("pqsqgworkstationconfig")
@Tag(name = "质量门功能配置服务", description = "质量门功能配置")
public class PqsQgWorkstationConfigController extends BaseController<PqsQgWorkstationConfigEntity> {

    private final IPqsQgWorkstationConfigService pqsQgWorkstationConfigService;

    @Autowired
    public PqsQgWorkstationConfigController(IPqsQgWorkstationConfigService pqsQgWorkstationConfigService) {
        this.crudService = pqsQgWorkstationConfigService;
        this.pqsQgWorkstationConfigService = pqsQgWorkstationConfigService;
    }

}