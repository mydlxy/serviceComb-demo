package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataScrEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataScrService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 工艺数据，拧紧
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehiclewodatascr")
@Tag(name = "工艺数据，拧紧")
public class EpsVehicleWoDataScrController extends BaseController<EpsVehicleWoDataScrEntity> {

    private final IEpsVehicleWoDataScrService epsVehicleWoDataScrService;

    @Autowired
    public EpsVehicleWoDataScrController(IEpsVehicleWoDataScrService epsVehicleWoDataScrService) {
        this.crudService = epsVehicleWoDataScrService;
        this.epsVehicleWoDataScrService = epsVehicleWoDataScrService;
    }

}