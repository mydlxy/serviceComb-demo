package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmMaterialSwitchoverConfigEntity;
import com.ca.mfd.prc.pm.service.IPmMaterialSwitchoverConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 物料切换配置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmmaterialswitchoverconfig")
@Tag(name = "物料切换配置")
public class PmMaterialSwitchoverConfigController extends BaseController<PmMaterialSwitchoverConfigEntity> {

    private final IPmMaterialSwitchoverConfigService pmMaterialSwitchoverConfigService;

    @Autowired
    public PmMaterialSwitchoverConfigController(IPmMaterialSwitchoverConfigService pmMaterialSwitchoverConfigService) {
        this.crudService = pmMaterialSwitchoverConfigService;
        this.pmMaterialSwitchoverConfigService = pmMaterialSwitchoverConfigService;
    }

}