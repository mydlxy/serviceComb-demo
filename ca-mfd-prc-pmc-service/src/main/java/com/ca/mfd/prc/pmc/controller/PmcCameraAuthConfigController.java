package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pmc.entity.PmcCameraAuthConfigEntity;
import com.ca.mfd.prc.pmc.service.IPmcCameraAuthConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 摄像机权限配置;Controller
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@RestController
@RequestMapping("equality.mes.pmc/{version}/pmccameraauthconfig")
@Tag(name = "摄像机权限配置;服务", description = "摄像机权限配置;")
public class PmcCameraAuthConfigController extends BaseController<PmcCameraAuthConfigEntity> {

    private final IPmcCameraAuthConfigService pmcCameraAuthConfigService;

    @Autowired
    public PmcCameraAuthConfigController(IPmcCameraAuthConfigService pmcCameraAuthConfigService) {
        this.crudService = pmcCameraAuthConfigService;
        this.pmcCameraAuthConfigService = pmcCameraAuthConfigService;
    }

}