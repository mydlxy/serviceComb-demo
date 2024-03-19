package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysVersionEntity;
import com.ca.mfd.prc.core.main.service.ISysVersionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 现场设备版本
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysversion")
@Tag(name = "现场设备版本")
public class SysVersionController extends BaseController<SysVersionEntity> {

    private final ISysVersionService sysVersionService;

    @Autowired
    public SysVersionController(ISysVersionService sysVersionService) {
        this.crudService = sysVersionService;
        this.sysVersionService = sysVersionService;
    }

}