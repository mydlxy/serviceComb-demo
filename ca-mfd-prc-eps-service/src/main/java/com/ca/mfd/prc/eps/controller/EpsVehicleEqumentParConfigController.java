package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentParConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleEqumentParConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 追溯设备工艺参数
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehicleequmentparconfig")
@Tag(name = "追溯设备工艺参数")
public class EpsVehicleEqumentParConfigController extends BaseController<EpsVehicleEqumentParConfigEntity> {

    private final IEpsVehicleEqumentParConfigService epsVehicleEqumentParConfigService;

    @Autowired
    public EpsVehicleEqumentParConfigController(IEpsVehicleEqumentParConfigService epsVehicleEqumentParConfigService) {
        this.crudService = epsVehicleEqumentParConfigService;
        this.epsVehicleEqumentParConfigService = epsVehicleEqumentParConfigService;
    }

}