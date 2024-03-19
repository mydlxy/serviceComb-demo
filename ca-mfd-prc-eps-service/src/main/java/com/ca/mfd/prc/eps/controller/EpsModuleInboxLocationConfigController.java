package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleInboxLocationConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleInboxLocationConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 预成组入箱位置配置Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmoduleinboxlocationconfig")
@Tag(name = "预成组入箱位置配置服务", description = "预成组入箱位置配置")
public class EpsModuleInboxLocationConfigController extends BaseController<EpsModuleInboxLocationConfigEntity> {

    private IEpsModuleInboxLocationConfigService epsModuleInboxLocationConfigService;

    @Autowired
    public EpsModuleInboxLocationConfigController(IEpsModuleInboxLocationConfigService epsModuleInboxLocationConfigService) {
        this.crudService = epsModuleInboxLocationConfigService;
        this.epsModuleInboxLocationConfigService = epsModuleInboxLocationConfigService;
    }

}