package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.dto.RoleDTO;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.service.IPrmRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 角色表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmrole")
@Tag(name = "角色表")
public class PrmRoleController extends BaseController<PrmRoleEntity> {

    private final IPrmRoleService prmRoleService;
    @Autowired
    private IdentityHelper identityHelper;

    @Autowired
    public PrmRoleController(IPrmRoleService prmRoleService) {
        this.crudService = prmRoleService;
        this.prmRoleService = prmRoleService;
    }

    @GetMapping(value = "/getcombodata")
    @Operation(summary = "获取所有数据")
    public ResultVO getComboData() {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        List<ComboInfoDTO> datas = prmRoleService.getData(null)
                .stream().sorted(Comparator.comparing(PrmRoleEntity::getGroupName)
                        .thenComparing(PrmRoleEntity::getRoleName))
                .map(o -> {
                    ComboInfoDTO et = new ComboInfoDTO();
                    et.setText("[" + o.getGroupName() + "]" + o.getRoleName());
                    et.setValue(o.getId().toString());
                    return et;
                }).collect(Collectors.toList());
        return result.ok(datas);
    }

    @PostMapping(value = "getdata")
    @Operation(summary = "获取所有数据")
    @Override
    public ResultVO<List<PrmRoleEntity>> page(@RequestBody DataDto model) {
        List<PrmRoleEntity> list = prmRoleService.list(model);
        return new ResultVO<List<PrmRoleEntity>>().ok(list, "获取数据成功");
    }

    @PostMapping(value = "del")
    @Operation(summary = "删除")
    @Override
    public ResultVO delete(@RequestBody IdsModel model) {
        //效验数据
        prmRoleService.delete(model.getIds());
        prmRoleService.saveChange();
        return new ResultVO<String>().ok("", "保存数据成功");
    }

    @GetMapping(value = "/gettreedata")
    @Operation(summary = "获取角色树")
    public ResultVO getTreeData() {
        List<TreeNode> rootNodes = new ArrayList<>();
        List<PrmRoleEntity> datas = prmRoleService.getData(null);
        datas.sort(Comparator.comparing(PrmRoleEntity::getRoleName));

        List<String> groupNames = datas.stream().map(o -> o.getGroupName()).distinct().
                sorted().collect(Collectors.toList());

        for (String groupName : groupNames) {
            TreeNode rootNode = new TreeNode();
            rootNode.setId(UUIDUtils.getGuid());
            rootNode.setChildren(new ArrayList<>());
            rootNode.setText(groupName);
            rootNode.setExtendData("Group");
            rootNode.setIconCls("fa fa-th-large");

            List<PrmRoleEntity> roleDatas = datas.stream().filter(o -> StringUtils.equals(o.getGroupName(), groupName))
                    .collect(Collectors.toList());
            for (PrmRoleEntity roleData : roleDatas) {
                TreeNode roleNode = new TreeNode();
                roleNode.setId(roleData.getId().toString());
                roleNode.setCode(roleData.getRoleCode());
                roleNode.setChildren(new ArrayList<>());
                roleNode.setText(roleData.getRoleName());
                roleNode.setExtendData(roleData);
                roleNode.setGroupName(roleData.getGroupName());
                roleNode.setDescription(roleData.getRemark());
                roleNode.setLeaf(true);
                roleNode.setIconCls("fa fa-user-circle-o");

                rootNode.getChildren().add(roleNode);
            }
            rootNodes.add(rootNode);
        }

        ResultVO<List<TreeNode>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(rootNodes);
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存角色")
    public ResultVO save(@RequestBody RoleDTO.RoleSaveModel model) {
        if (StringUtils.isBlank(model.getName())) {
            throw new InkelinkException("角色名称不能为空");
        }
        if (model.getService().getId() == null || model.getService().getId() <= 0) {
            prmRoleService.insert(model.getService());
        } else {
            prmRoleService.update(model.getService());
        }
        prmRoleService.saveChange();
        ResultVO<Boolean> result = new ResultVO<>();
        result.setMessage("保存数据成功");
        return result.ok(true);
    }

    @GetMapping(value = "/getpermissons")
    @Operation(summary = "获取权限")
    public ResultVO getPermissons(String roleId) {
        ResultVO<List<PrmPermissionEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(prmRoleService.getPermissons(roleId));
    }

    @GetMapping(value = "/exportpermission")
    @Operation(summary = "导出权限")
    public void exportPermission(String roleId, HttpServletResponse response) throws IOException {
        PrmRoleEntity roleData = prmRoleService.get(roleId);
        String fileName = roleData.getGroupName() + "(" + roleData.getRoleName() + ")权限" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        prmRoleService.exportPermesion(roleId, fileName, response);
    }

    @GetMapping(value = "/exportusers")
    @Operation(summary = "根据角色ID导出角色")
    public void exportUsers(String roleId, HttpServletResponse response) throws IOException {
        PrmRoleEntity roleData = prmRoleService.get(roleId);
        String fileName = roleData.getGroupName() + "(" + roleData.getRoleName() + ")人员" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        prmRoleService.exportUsers(roleId, fileName, response);
    }

    @GetMapping(value = "/getroleidbyusers")
    @Operation(summary = "查看设置当前角色用户")
    public ResultVO getRoleIdByUsers(String id) {
        List<PrmUserEntity> data = prmRoleService.getUsers(id);
        ResultVO<List<PrmUserEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @GetMapping(value = "/exportallpermission")
    @Operation(summary = "导出角色权限列表")
    public void exportAllPermission(HttpServletResponse response) throws IOException {
        String fileName = "角色权限列表" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        prmRoleService.exportAllPermesionByRole(fileName, response);
    }

    @GetMapping(value = "/exportallusers")
    @Operation(summary = "导出角色用户列表")
    public void exportAllUsers(HttpServletResponse response) throws IOException {
        String fileName = "角色用户列表" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        prmRoleService.exportAllUsersByRole(fileName, response);
    }

}