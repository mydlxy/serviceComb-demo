package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikWorkstationService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺百格图-关联的岗位Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexqualitymatrikworkstation")
@Tag(name = "精致工艺百格图-关联的岗位服务", description = "精致工艺百格图-关联的岗位")
public class PqsExQualityMatrikWorkstationController extends BaseController<PqsExQualityMatrikWorkstationEntity> {

    private IPqsExQualityMatrikWorkstationService pqsExQualityMatrikWorkstationService;

    @Autowired
    public PqsExQualityMatrikWorkstationController(IPqsExQualityMatrikWorkstationService pqsExQualityMatrikWorkstationService) {
        this.crudService = pqsExQualityMatrikWorkstationService;
        this.pqsExQualityMatrikWorkstationService = pqsExQualityMatrikWorkstationService;
    }

}