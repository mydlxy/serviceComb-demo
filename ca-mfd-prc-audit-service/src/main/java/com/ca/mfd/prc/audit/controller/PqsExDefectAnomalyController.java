package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExDefectAnomalyEntity;
import com.ca.mfd.prc.audit.service.IPqsExDefectAnomalyService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author inkelink
 * @Description: 精艺检修缺陷库配置Controller
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
@RestController
@RequestMapping("pqsexdefectanomaly")
@Tag(name = "精艺检修缺陷库配置服务", description = "精艺检修缺陷库配置")
public class PqsExDefectAnomalyController extends BaseController<PqsExDefectAnomalyEntity> {

    private IPqsExDefectAnomalyService pqsExDefectAnomalyService;

    @Autowired
    public PqsExDefectAnomalyController(IPqsExDefectAnomalyService pqsExDefectAnomalyService) {
        this.crudService = pqsExDefectAnomalyService;
        this.pqsExDefectAnomalyService = pqsExDefectAnomalyService;
    }

}