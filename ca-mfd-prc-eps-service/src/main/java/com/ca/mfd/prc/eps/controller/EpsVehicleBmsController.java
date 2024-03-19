package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleBmsEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleBmsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 车辆电检数据
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehiclebms")
@Tag(name = "车辆电检数据")
public class EpsVehicleBmsController extends BaseController<EpsVehicleBmsEntity> {

    private final IEpsVehicleBmsService epsVehicleBmsService;

    @Autowired
    public EpsVehicleBmsController(IEpsVehicleBmsService epsVehicleBmsService) {
        this.crudService = epsVehicleBmsService;
        this.epsVehicleBmsService = epsVehicleBmsService;
    }

}