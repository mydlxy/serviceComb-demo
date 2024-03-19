package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysWhitelistEntity;
import com.ca.mfd.prc.core.main.service.ISysWhitelistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 白名单
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/syswhitelist")
@Tag(name = "白名单")
public class SysWhitelistController extends BaseController<SysWhitelistEntity> {

    private final ISysWhitelistService sysWhitelistService;

    @Autowired
    public SysWhitelistController(ISysWhitelistService sysWhitelistService) {
        this.crudService = sysWhitelistService;
        this.sysWhitelistService = sysWhitelistService;
    }

}