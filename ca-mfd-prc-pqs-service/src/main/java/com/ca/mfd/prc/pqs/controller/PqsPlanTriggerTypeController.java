package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsPlanTriggerTypeEntity;
import com.ca.mfd.prc.pqs.service.IPqsPlanTriggerTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 检验计划触发类型配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsplantriggertype")
@Tag(name = "检验计划触发类型配置服务", description = "检验计划触发类型配置")
public class PqsPlanTriggerTypeController extends BaseController<PqsPlanTriggerTypeEntity> {

    private final IPqsPlanTriggerTypeService pqsPlanTriggerTypeService;

    @Autowired
    public PqsPlanTriggerTypeController(IPqsPlanTriggerTypeService pqsPlanTriggerTypeService) {
        this.crudService = pqsPlanTriggerTypeService;
        this.pqsPlanTriggerTypeService = pqsPlanTriggerTypeService;
    }

}