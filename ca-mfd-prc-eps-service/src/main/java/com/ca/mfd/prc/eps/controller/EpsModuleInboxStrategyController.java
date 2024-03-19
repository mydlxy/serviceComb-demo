package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleInboxStrategyEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleInboxStrategyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 预成组入箱策略Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmoduleinboxstrategy")
@Tag(name = "预成组入箱策略服务", description = "预成组入箱策略")
public class EpsModuleInboxStrategyController extends BaseController<EpsModuleInboxStrategyEntity> {

    private IEpsModuleInboxStrategyService epsModuleInboxStrategyService;

    @Autowired
    public EpsModuleInboxStrategyController(IEpsModuleInboxStrategyService epsModuleInboxStrategyService) {
        this.crudService = epsModuleInboxStrategyService;
        this.epsModuleInboxStrategyService = epsModuleInboxStrategyService;
    }

}