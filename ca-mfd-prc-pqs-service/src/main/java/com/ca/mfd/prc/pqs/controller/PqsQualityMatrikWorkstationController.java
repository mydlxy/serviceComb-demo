package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikWorkstationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 百格图关联的岗位Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqualitymatrikworkstation")
@Tag(name = "百格图关联的岗位服务", description = "百格图关联的岗位")
public class PqsQualityMatrikWorkstationController extends BaseController<PqsQualityMatrikWorkstationEntity> {

    private final IPqsQualityMatrikWorkstationService pqsQualityMatrikWorkstationService;

    @Autowired
    public PqsQualityMatrikWorkstationController(IPqsQualityMatrikWorkstationService pqsQualityMatrikWorkstationService) {
        this.crudService = pqsQualityMatrikWorkstationService;
        this.pqsQualityMatrikWorkstationService = pqsQualityMatrikWorkstationService;
    }

}