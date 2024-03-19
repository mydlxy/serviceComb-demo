package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysLicenseNotifierEntity;
import com.ca.mfd.prc.core.main.service.ISysLicenseNotifierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 授权码自动配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/syslicensenotifier")
@Tag(name = "授权码自动配置")
public class SysLicenseNotifierController extends BaseController<SysLicenseNotifierEntity> {

    private final ISysLicenseNotifierService sysLicenseNotifierService;

    @Autowired
    public SysLicenseNotifierController(ISysLicenseNotifierService sysLicenseNotifierService) {
        this.crudService = sysLicenseNotifierService;
        this.sysLicenseNotifierService = sysLicenseNotifierService;
    }

}