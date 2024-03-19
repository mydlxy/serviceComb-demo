package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataShootEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataShootService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 整车三电系统拍摄追溯
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehiclewodatashoot")
@Tag(name = "整车三电系统拍摄追溯")
public class EpsVehicleWoDataShootController extends BaseController<EpsVehicleWoDataShootEntity> {

    private final IEpsVehicleWoDataShootService epsVehicleWoDataShootService;

    @Autowired
    public EpsVehicleWoDataShootController(IEpsVehicleWoDataShootService epsVehicleWoDataShootService) {
        this.crudService = epsVehicleWoDataShootService;
        this.epsVehicleWoDataShootService = epsVehicleWoDataShootService;
    }

}