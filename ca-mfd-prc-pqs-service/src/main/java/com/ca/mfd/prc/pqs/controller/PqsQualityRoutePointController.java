package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityRoutePointEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityRoutePointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 车辆去向点位配置表Controller
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
@RestController
@RequestMapping("pqsqualityroutepoint")
@Tag(name = "车辆去向点位配置表服务", description = "车辆去向点位配置表")
public class PqsQualityRoutePointController extends BaseController<PqsQualityRoutePointEntity> {

    private final IPqsQualityRoutePointService pqsQualityRoutePointService;

    @Autowired
    public PqsQualityRoutePointController(IPqsQualityRoutePointService pqsQualityRoutePointService) {
        this.crudService = pqsQualityRoutePointService;
        this.pqsQualityRoutePointService = pqsQualityRoutePointService;
    }

}