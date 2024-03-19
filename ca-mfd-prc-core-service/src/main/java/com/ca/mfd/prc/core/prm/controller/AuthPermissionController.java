package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.service.IPrmInterfacePermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmRolePermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmRoleService;
import com.ca.mfd.prc.core.prm.service.IPrmSessionService;
import com.ca.mfd.prc.core.prm.service.IPrmUserOpenService;
import com.ca.mfd.prc.core.prm.service.IPrmUserPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmUserRoleService;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户登录
 *
 * @author inkelink
 */
@RestController
@RequestMapping("prm/authpermission")
@Tag(name = "用户登录")
public class AuthPermissionController extends BaseApiController {
    @Autowired
    IPrmSessionService prmSessionService;
    @Autowired
    IPrmPermissionService prmPermissionService;
    @Autowired
    IPrmUserService prmUserService;
    @Autowired
    IPrmUserPermissionService prmUserPermissionService;
    @Autowired
    IPrmUserOpenService prmUserOpenService;
    @Autowired
    IPrmUserRoleService prmUserRoleService;
    @Autowired
    IPrmRoleService prmRoleService;
    @Autowired
    IPrmRolePermissionService prmRolePermissionService;
    @Autowired
    IPrmInterfacePermissionService prmInterfacePermissionService;

    @PostMapping(value = "/allpermission")
    @Operation(summary = "登录")
    public ResultVO<List<String>> allPermission() {
        List<String> data = prmPermissionService.getAllDatas().stream().map(PrmPermissionEntity::getPermissionCode).collect(Collectors.toList());
        return new ResultVO<List<String>>().ok(data);
    }
}
