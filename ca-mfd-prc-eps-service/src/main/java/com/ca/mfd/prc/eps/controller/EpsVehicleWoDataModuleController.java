package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataModuleEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataModuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 电池模组入箱Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsvehiclewodatamodule")
@Tag(name = "电池模组入箱服务", description = "电池模组入箱")
public class EpsVehicleWoDataModuleController extends BaseController<EpsVehicleWoDataModuleEntity> {

    private IEpsVehicleWoDataModuleService epsVehicleWoDataModuleService;

    @Autowired
    public EpsVehicleWoDataModuleController(IEpsVehicleWoDataModuleService epsVehicleWoDataModuleService) {
        this.crudService = epsVehicleWoDataModuleService;
        this.epsVehicleWoDataModuleService = epsVehicleWoDataModuleService;
    }

}