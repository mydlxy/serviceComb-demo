package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoLogEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 工艺操作日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehiclewolog")
@Tag(name = "工艺操作日志")
public class EpsVehicleWoLogController extends BaseController<EpsVehicleWoLogEntity> {

    private final IEpsVehicleWoLogService epsVehicleWoLogService;

    @Autowired
    public EpsVehicleWoLogController(IEpsVehicleWoLogService epsVehicleWoLogService) {
        this.crudService = epsVehicleWoLogService;
        this.epsVehicleWoLogService = epsVehicleWoLogService;
    }


}