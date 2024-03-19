package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleCarrierEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleCarrierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 预成组物料载具Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmodulecarrier")
@Tag(name = "预成组物料载具服务", description = "预成组物料载具")
public class EpsModuleCarrierController extends BaseController<EpsModuleCarrierEntity> {

    private IEpsModuleCarrierService epsModuleCarrierService;

    @Autowired
    public EpsModuleCarrierController(IEpsModuleCarrierService epsModuleCarrierService) {
        this.crudService = epsModuleCarrierService;
        this.epsModuleCarrierService = epsModuleCarrierService;
    }

}