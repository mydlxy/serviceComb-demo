package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoExecuteEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoExecuteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 车辆操作执行信息Controller
 * @date 2023年09月14日
 * @变更说明 BY inkelink At 2023年09月14日
 */
@RestController
@RequestMapping("epsvehiclewoexecute")
@Tag(name = "车辆操作执行信息服务", description = "车辆操作执行信息")
public class EpsVehicleWoExecuteController extends BaseController<EpsVehicleWoExecuteEntity> {

    private final IEpsVehicleWoExecuteService epsVehicleWoExecuteService;

    @Autowired
    public EpsVehicleWoExecuteController(IEpsVehicleWoExecuteService epsVehicleWoExecuteService) {
        this.crudService = epsVehicleWoExecuteService;
        this.epsVehicleWoExecuteService = epsVehicleWoExecuteService;
    }

}