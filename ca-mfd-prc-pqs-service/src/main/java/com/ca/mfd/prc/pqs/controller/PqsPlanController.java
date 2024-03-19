package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsPlanEntity;
import com.ca.mfd.prc.pqs.service.IPqsPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 检验计划配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsplan")
@Tag(name = "检验计划配置服务", description = "检验计划配置")
public class PqsPlanController extends BaseController<PqsPlanEntity> {

    private final IPqsPlanService pqsPlanService;

    @Autowired
    public PqsPlanController(IPqsPlanService pqsPlanService) {
        this.crudService = pqsPlanService;
        this.pqsPlanService = pqsPlanService;
    }

}