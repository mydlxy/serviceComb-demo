package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentHoldEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleEqumentHoldService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 采集暂存数据Controller
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@RestController
@RequestMapping("epsvehicleequmenthold")
@Tag(name = "采集暂存数据服务", description = "采集暂存数据")
public class EpsVehicleEqumentHoldController extends BaseController<EpsVehicleEqumentHoldEntity> {

    private final IEpsVehicleEqumentHoldService epsVehicleEqumentHoldService;

    @Autowired
    public EpsVehicleEqumentHoldController(IEpsVehicleEqumentHoldService epsVehicleEqumentHoldService) {
        this.crudService = epsVehicleEqumentHoldService;
        this.epsVehicleEqumentHoldService = epsVehicleEqumentHoldService;
    }

}