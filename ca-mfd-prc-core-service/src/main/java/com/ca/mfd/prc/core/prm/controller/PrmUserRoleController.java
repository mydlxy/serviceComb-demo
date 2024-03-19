package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.prm.entity.PrmUserRoleEntity;
import com.ca.mfd.prc.core.prm.service.IPrmUserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户角色表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmuserrole")
@Tag(name = "用户角色表")
public class PrmUserRoleController extends BaseController<PrmUserRoleEntity> {

    private final IPrmUserRoleService prmUserRoleService;

    @Autowired
    public PrmUserRoleController(IPrmUserRoleService prmUserRoleService) {
        this.crudService = prmUserRoleService;
        this.prmUserRoleService = prmUserRoleService;
    }

}