package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysMenuItemEntity;
import com.ca.mfd.prc.core.main.service.ISysMenuItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 菜单项
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysmenuitem")
@Tag(name = "菜单项")
public class SysMenuItemController extends BaseController<SysMenuItemEntity> {

    private final ISysMenuItemService sysMenuItemService;

    @Autowired
    public SysMenuItemController(ISysMenuItemService sysMenuItemService) {
        this.crudService = sysMenuItemService;
        this.sysMenuItemService = sysMenuItemService;
    }

}