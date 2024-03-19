package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleElementScrapEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleElementScrapService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 模组元素报废Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmoduleelementscrap")
@Tag(name = "模组元素报废服务", description = "模组元素报废")
public class EpsModuleElementScrapController extends BaseController<EpsModuleElementScrapEntity> {

    private IEpsModuleElementScrapService epsModuleElementScrapService;

    @Autowired
    public EpsModuleElementScrapController(IEpsModuleElementScrapService epsModuleElementScrapService) {
        this.crudService = epsModuleElementScrapService;
        this.epsModuleElementScrapService = epsModuleElementScrapService;
    }

}