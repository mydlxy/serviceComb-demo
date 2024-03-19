package com.ca.mfd.prc.core.prm.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.dto.PrmTemporaryPermissionsVO;
import com.ca.mfd.prc.core.prm.dto.PrmUserPermissionModel;
import com.ca.mfd.prc.core.prm.dto.PrmUserPermissionsVO;
import com.ca.mfd.prc.core.prm.dto.PrmUserTempPermission;
import com.ca.mfd.prc.core.prm.dto.PrmUserTemporaryPermissionsVO;
import com.ca.mfd.prc.core.prm.entity.PrmUserPermissionEntity;
import com.ca.mfd.prc.core.prm.service.IPrmUserPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户权限关联表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmuserpermission")
@Tag(name = "用户权限关联表")
public class PrmUserPermissionController extends BaseController<PrmUserPermissionEntity> {

    private final IPrmUserPermissionService prmUserPermissionService;
    @Autowired
    private IPrmUserService prmUserService;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    public PrmUserPermissionController(IPrmUserPermissionService prmUserPermissionService) {
        this.crudService = prmUserPermissionService;
        this.prmUserPermissionService = prmUserPermissionService;
    }

    @GetMapping(value = "/getusertemporarypermissions")
    @Operation(summary = "获取用户的临时权限")
    public ResultVO getUserTemporaryPermissions(String userId) {
        ResultVO<List<PrmUserTemporaryPermissionsVO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        List<PrmUserTemporaryPermissionsVO> datas = new ArrayList<>();
        if (!StringUtils.isBlank(userId)) {
            datas = prmUserPermissionService.getUserTemporaryPermissions(userId)
                    .stream().map(c -> {
                        PrmUserTemporaryPermissionsVO et = new PrmUserTemporaryPermissionsVO();
                        et.setId(c.getId().toString());
                        et.setCode(c.getPermissionCode());
                        et.setName(c.getPermissionName());
                        et.setDescription(c.getRemark());
                        et.setModel(c.getModel());
                        et.setRecycleDt(c.getRecycleDt());
                        return et;
                    }).collect(Collectors.toList());
            List<String> permissions = identityHelper.getLoginUser().getPermissions();
            if (permissions == null) {
                permissions = new ArrayList<>();
            }
            List<String> finalPermissions = permissions;
            datas = datas.stream().filter(o -> finalPermissions.contains(o.getCode())).collect(Collectors.toList());
        }
        return result.ok(datas);
    }

    @PostMapping(value = "save")
    @Operation(summary = "保存权限")
    public ResultVO save(@RequestBody PrmUserPermissionModel prmUserPermissionModel) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("保存成功");
        if (prmUserPermissionModel.getPermissions() == null) {
            prmUserPermissionModel.setPermissions(new ArrayList<>());
        }
        prmUserPermissionService.save(prmUserPermissionModel.getPermissions(), prmUserPermissionModel.getId());
        prmUserPermissionService.saveChange();
        return result.ok("");
    }

    @GetMapping(value = "savepermission")
    @Operation(summary = "保存用户权限")
    public ResultVO savePermission(String userId, String permissionId, Date recycleDt) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("保存成功");
        //获取现有的数据
        PrmUserPermissionEntity data = prmUserPermissionService.getUserFirst(userId, permissionId);
        //删除数据
        if (recycleDt == null) {
            if (data != null) {
                UpdateWrapper<PrmUserPermissionEntity> delset = new UpdateWrapper<>();
                delset.lambda().eq(PrmUserPermissionEntity::getId, data.getId());
                prmUserPermissionService.delete(delset);
            }
        } else {
            if (data != null) {
                UpdateWrapper<PrmUserPermissionEntity> upset = new UpdateWrapper<>();
                upset.lambda().set(PrmUserPermissionEntity::getRecycleDt, recycleDt)
                        .eq(PrmUserPermissionEntity::getId, data.getId());
                prmUserPermissionService.update(upset);
            } else {
                PrmUserPermissionEntity et = new PrmUserPermissionEntity();
                et.setPrcPrmUserId(Long.valueOf(userId));
                et.setPrcPrmPermissionId(Long.valueOf(permissionId));
                et.setRecycleDt(recycleDt);
                prmUserPermissionService.insert(et);
            }
        }
        prmUserPermissionService.saveChange();
        return result.ok("");
    }

    @PostMapping(value = "exportdatas")
    @Operation(summary = "导出权限")
    public void exportDatas(HttpServletResponse response) throws IOException {
        String fileName = "用户临时权限列表";
        prmUserPermissionService.exportDatas(fileName, response);
    }

    @PostMapping(value = "getprmuserdata")
    @Operation(summary = "获取用户权限")
    public ResultVO getPrmuserData() {
        ResultVO<List<PrmUserPermissionsVO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        List<PrmUserPermissionsVO> list = prmUserPermissionService.getPrmUserInfos()
                .stream().map(c -> {
                    PrmUserPermissionsVO et = new PrmUserPermissionsVO();
                    et.setId(c.getId().toString());
                    et.setFullName(c.getNickName() + "/" + c.getUserName());
                    et.setUserName(c.getUserName());
                    et.setNickName(c.getNickName());
                    et.setGroupName(c.getGroupName());
                    et.setEnGroupName(c.getEnGroupName());
                    et.setCnGroupName(c.getGroupName());
                    et.setIdCard(c.getIdCard());
                    et.setEmail(c.getEmail());
                    et.setPhone(c.getPhone());
                    et.setEnName(c.getEnName());
                    et.setCnName(c.getCnName());
                    et.setCode(c.getJobNo());
                    et.setNo(c.getNo());
                    et.setLastUpdatedBy(c.getLastUpdatedUser());
                    et.setCreatedBy(c.getCreatedUser());
                    et.setLastUpdatedDate(c.getLastUpdateDate());
                    et.setRoles(c.getRoles());
                    et.setPassExpireDt(c.getPassExpireDt());
                    et.setRoleGroupName(c.getRoleGroupName());
                    et.setRoleName(c.getRoleName());
                    et.setCreationDate(c.getCreationDate());
                    et.setIsActive(c.getIsActive());
                    et.setRemark(c.getRemark());
                    return et;
                }).collect(Collectors.toList());

        List<PrmUserPermissionsVO> datas = list.stream().filter(o ->
                        StringUtils.isNotBlank(o.getId())
                                && !StringUtils.equals(o.getUserName(), Constant.SYSTEM_ADMINROLE)
                                && !StringUtils.equals(o.getUserName(), Constant.SYSTEM_MANAGER))
                .collect(Collectors.toList());
        return result.ok(datas);
    }

    @PostMapping(value = "getprmpermissionentitys")
    @Operation(summary = "获取用户临时权限")
    public ResultVO getPrmPermissionEntitys(@RequestBody PrmUserTempPermission prmUser) {
        ResultVO<List<PrmTemporaryPermissionsVO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        List<PrmTemporaryPermissionsVO> datas = prmUserPermissionService.getPrmPermissionEntitys(prmUser.getUserId())
                .stream().map(c -> {
                    PrmTemporaryPermissionsVO et = new PrmTemporaryPermissionsVO();
                    et.setId(c.getId().toString());
                    et.setPermissionName(c.getPermissionName());
                    et.setPermissionModel(c.getPermissionModel());
                    et.setName(c.getPermissionName());
                    et.setUserName(c.getUserName());
                    et.setUserNo(c.getUserNo());
                    et.setModel(c.getModel());
                    et.setRecycleDt(c.getRecycleDt());
                    return et;
                }).collect(Collectors.toList());
        return result.ok(datas);
    }
}