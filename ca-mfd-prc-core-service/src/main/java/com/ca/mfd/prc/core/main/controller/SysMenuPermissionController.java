package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysMenuPermissionEntity;
import com.ca.mfd.prc.core.main.service.ISysMenuPermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 菜单权限
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysmenupermission")
@Tag(name = "菜单权限")
public class SysMenuPermissionController extends BaseController<SysMenuPermissionEntity> {

    private final ISysMenuPermissionService sysMenuPermissionService;

    @Autowired
    public SysMenuPermissionController(ISysMenuPermissionService sysMenuPermissionService) {
        this.crudService = sysMenuPermissionService;
        this.sysMenuPermissionService = sysMenuPermissionService;
    }

}