package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.prm.entity.PrmRolePermissionEntity;
import com.ca.mfd.prc.core.prm.service.IPrmRolePermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 角色权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmrolepermission")
@Tag(name = "角色权限表")
public class PrmRolePermissionController extends BaseController<PrmRolePermissionEntity> {

    private final IPrmRolePermissionService prmRolePermissionService;

    @Autowired
    public PrmRolePermissionController(IPrmRolePermissionService prmRolePermissionService) {
        this.crudService = prmRolePermissionService;
        this.prmRolePermissionService = prmRolePermissionService;
    }

}