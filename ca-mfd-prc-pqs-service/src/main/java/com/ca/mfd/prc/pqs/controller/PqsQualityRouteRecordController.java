package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityRouteRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityRouteRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 车辆去向指定记录Controller
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
@RestController
@RequestMapping("pqsqualityrouterecord")
@Tag(name = "车辆去向指定记录服务", description = "车辆去向指定记录")
public class PqsQualityRouteRecordController extends BaseController<PqsQualityRouteRecordEntity> {

    private final IPqsQualityRouteRecordService pqsQualityRouteRecordService;

    @Autowired
    public PqsQualityRouteRecordController(IPqsQualityRouteRecordService pqsQualityRouteRecordService) {
        this.crudService = pqsQualityRouteRecordService;
        this.pqsQualityRouteRecordService = pqsQualityRouteRecordService;
    }

}