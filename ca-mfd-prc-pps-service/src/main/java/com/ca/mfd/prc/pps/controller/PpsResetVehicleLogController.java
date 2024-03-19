package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsResetVehicleLogEntity;
import com.ca.mfd.prc.pps.service.IPpsResetVehicleLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 重置车辆日志Controller
 * @date 2023年08月25日
 * @变更说明 BY inkelink At 2023年08月25日
 */
@RestController
@RequestMapping("ppsresetvehiclelog")
@Tag(name = "重置车辆日志服务", description = "重置车辆日志")
public class PpsResetVehicleLogController extends BaseController<PpsResetVehicleLogEntity> {

    private final IPpsResetVehicleLogService ppsResetVehicleLogService;

    @Autowired
    public PpsResetVehicleLogController(IPpsResetVehicleLogService ppsResetVehicleLogService) {
        this.crudService = ppsResetVehicleLogService;
        this.ppsResetVehicleLogService = ppsResetVehicleLogService;
    }

}