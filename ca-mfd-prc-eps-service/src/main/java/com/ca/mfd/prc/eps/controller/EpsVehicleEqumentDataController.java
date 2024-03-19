package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentDataEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleEqumentDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 追溯设备采集记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehicleequmentdata")
@Tag(name = "追溯设备采集记录")
public class EpsVehicleEqumentDataController extends BaseController<EpsVehicleEqumentDataEntity> {

    private final IEpsVehicleEqumentDataService epsVehicleEqumentDataService;

    @Autowired
    public EpsVehicleEqumentDataController(IEpsVehicleEqumentDataService epsVehicleEqumentDataService) {
        this.crudService = epsVehicleEqumentDataService;
        this.epsVehicleEqumentDataService = epsVehicleEqumentDataService;
    }

}