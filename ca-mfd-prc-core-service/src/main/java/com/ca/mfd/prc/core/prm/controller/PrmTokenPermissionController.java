package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.prm.entity.PrmTokenPermissionEntity;
import com.ca.mfd.prc.core.prm.service.IPrmTokenPermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 令牌权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmtokenpermission")
@Tag(name = "令牌权限表")
public class PrmTokenPermissionController extends BaseController<PrmTokenPermissionEntity> {

    private final IPrmTokenPermissionService prmTokenPermissionService;

    @Autowired
    public PrmTokenPermissionController(IPrmTokenPermissionService prmTokenPermissionService) {
        this.crudService = prmTokenPermissionService;
        this.prmTokenPermissionService = prmTokenPermissionService;
    }

}