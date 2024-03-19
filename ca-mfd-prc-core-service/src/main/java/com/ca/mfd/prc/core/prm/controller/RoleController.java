package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.dto.RoleView;
import com.ca.mfd.prc.core.prm.service.IPrmRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色
 *
 * @author inkelink ${email}
 * @date 2023-07-24
 */
@RestController
@RequestMapping("member/ucenter/role")
@Tag(name = "角色")
public class RoleController extends BaseApiController {

    @Autowired
    private IPrmRoleService prmRoleService;

    @GetMapping(value = "name")
    @Operation(summary = "获取用户授权信息")
    public ResultVO name() {
        List<RoleView> list = prmRoleService.getData(null).stream().map(c -> {
            RoleView et = new RoleView();
            et.setId(c.getId().toString());
            et.setCode(c.getRoleCode());
            et.setName(c.getRoleName());
            return et;
        }).collect(Collectors.toList());
        return new ResultVO<List<RoleView>>().ok(list, "获取数据成功");
    }

}