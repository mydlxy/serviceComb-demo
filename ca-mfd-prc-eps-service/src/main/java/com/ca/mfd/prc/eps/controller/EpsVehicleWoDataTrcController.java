package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataTrcEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataTrcService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 追溯操作记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehiclewodatatrc")
@Tag(name = "追溯操作记录")
public class EpsVehicleWoDataTrcController extends BaseController<EpsVehicleWoDataTrcEntity> {

    private final IEpsVehicleWoDataTrcService epsVehicleWoDataTrcService;

    @Autowired
    public EpsVehicleWoDataTrcController(IEpsVehicleWoDataTrcService epsVehicleWoDataTrcService) {
        this.crudService = epsVehicleWoDataTrcService;
        this.epsVehicleWoDataTrcService = epsVehicleWoDataTrcService;
    }
    //getpagedata AllowAnonymous

}