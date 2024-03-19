package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleMaterialBindingEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleMaterialBindingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 模组绑定物料Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmodulematerialbinding")
@Tag(name = "模组绑定物料服务", description = "模组绑定物料")
public class EpsModuleMaterialBindingController extends BaseController<EpsModuleMaterialBindingEntity> {

    private IEpsModuleMaterialBindingService epsModuleMaterialBindingService;

    @Autowired
    public EpsModuleMaterialBindingController(IEpsModuleMaterialBindingService epsModuleMaterialBindingService) {
        this.crudService = epsModuleMaterialBindingService;
        this.epsModuleMaterialBindingService = epsModuleMaterialBindingService;
    }

}