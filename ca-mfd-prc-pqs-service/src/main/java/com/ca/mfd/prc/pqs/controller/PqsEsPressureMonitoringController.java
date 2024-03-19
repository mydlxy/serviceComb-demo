package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsPressureMonitoringEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsPressureMonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 轮胎气压监测Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsespressuremonitoring")
@Tag(name = "轮胎气压监测服务", description = "轮胎气压监测")
public class PqsEsPressureMonitoringController extends BaseWithDefValController<PqsEsPressureMonitoringEntity> {

    private IPqsEsPressureMonitoringService pqsEsPressureMonitoringService;

    @Autowired
    public PqsEsPressureMonitoringController(IPqsEsPressureMonitoringService pqsEsPressureMonitoringService) {
        this.crudService = pqsEsPressureMonitoringService;
        this.pqsEsPressureMonitoringService = pqsEsPressureMonitoringService;
    }

}