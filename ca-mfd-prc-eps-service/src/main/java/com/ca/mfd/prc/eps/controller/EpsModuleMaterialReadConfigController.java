package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleMaterialReadConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleMaterialReadConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 电池模组物料用量读取配置Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmodulematerialreadconfig")
@Tag(name = "电池模组物料用量读取配置服务", description = "电池模组物料用量读取配置")
public class EpsModuleMaterialReadConfigController extends BaseController<EpsModuleMaterialReadConfigEntity> {

    private IEpsModuleMaterialReadConfigService epsModuleMaterialReadConfigService;

    @Autowired
    public EpsModuleMaterialReadConfigController(IEpsModuleMaterialReadConfigService epsModuleMaterialReadConfigService) {
        this.crudService = epsModuleMaterialReadConfigService;
        this.epsModuleMaterialReadConfigService = epsModuleMaterialReadConfigService;
    }

}