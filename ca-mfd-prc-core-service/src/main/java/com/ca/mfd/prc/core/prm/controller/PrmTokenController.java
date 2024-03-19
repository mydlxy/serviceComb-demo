package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.dto.TokenDTO;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmTokenEntity;
import com.ca.mfd.prc.core.prm.service.IPrmTokenService;
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
 * 令牌表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmtoken")
@Tag(name = "令牌表")
public class PrmTokenController extends BaseController<PrmTokenEntity> {

    private final IPrmTokenService prmTokenService;

    @Autowired
    public PrmTokenController(IPrmTokenService prmTokenService) {
        this.crudService = prmTokenService;
        this.prmTokenService = prmTokenService;
    }

    @PostMapping(value = "getdata")
    @Operation(summary = "获取所有数据")
    @Override
    public ResultVO<List<PrmTokenEntity>> page(@RequestBody DataDto model) {
        List<PrmTokenEntity> list = prmTokenService.list(model);
        return new ResultVO<List<PrmTokenEntity>>().ok(list, "获取数据成功");
    }

    @PostMapping(value = "del")
    @Operation(summary = "删除")
    @Override
    public ResultVO delete(@RequestBody IdsModel model) {
        //效验数据
        prmTokenService.delete(model.getIds());
        prmTokenService.saveChange();
        return new ResultVO<String>().ok("", "删除成功");
    }

    @GetMapping(value = "gettreedata")
    @Operation(summary = "获取角色树")
    public ResultVO getTreeData() {
        List<TreeNode> rootNodes = new ArrayList<>();
        List<PrmTokenEntity> datas = prmTokenService.getData(null);
        datas.sort(Comparator.comparing(PrmTokenEntity::getGroupName));

        List<String> groupNames = datas.stream().map(o -> o.getGroupName())
                .distinct().sorted().collect(Collectors.toList());
        for (String groupName : groupNames) {
            TreeNode rootNode = new TreeNode();
            rootNode.setId(UUIDUtils.getGuid());
            rootNode.setChildren(new ArrayList());
            rootNode.setText(groupName);
            rootNode.setExtendData("Group");
            rootNode.setIconCls("fa fa-th-large");

            List<PrmTokenEntity> roleDatas = datas.stream().filter(o -> StringUtils.equals(o.getGroupName(), groupName))
                    .collect(Collectors.toList());
            for (PrmTokenEntity roleData : roleDatas) {
                TreeNode roleNode = new TreeNode();
                roleNode.setId(roleData.getId().toString());
                roleNode.setCode(roleData.getToken());
                roleNode.setText(roleData.getTokenName());
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

    @PostMapping(value = "save")
    @Operation(summary = "保存角色")
    public ResultVO save(TokenDTO.TokenSaveModel model) {
        if (StringUtils.isBlank(model.getGroupName())) {
            throw new InkelinkException("角色名称不能为空");
        }
        if (model.getService().getId() == null || model.getService().getId() <= 0) {
            prmTokenService.insert(model.getService());
        } else {
            prmTokenService.update(model.getService());
        }
        prmTokenService.saveChange();
        ResultVO<Boolean> result = new ResultVO<>();
        result.setMessage("保存数据成功");
        return result.ok(true);
    }

    @GetMapping(value = "getpermissons")
    @Operation(summary = "获取权限")
    public ResultVO getPermissons(String tokenId) {
        ResultVO<List<PrmPermissionEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        return result.ok(prmTokenService.getPermissons(tokenId));
    }

    @GetMapping(value = "/exportpermission")
    @Operation(summary = "获取权限")
    public void exportPermission(String tokenId, HttpServletResponse response) throws IOException {
        PrmTokenEntity tokenData = prmTokenService.get(tokenId);
        String fileName = tokenData.getGroupName() + "(" + tokenData.getTokenName() + ")权限" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        prmTokenService.exportPermesion(tokenId, fileName, response);
    }

    @GetMapping(value = "/exportallpermission")
    @Operation(summary = "导出权限")
    public void exportAllPermission(HttpServletResponse response) throws IOException {
        String fileName = "角色权限列表" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        prmTokenService.exportAllPermesionByToken(fileName, response);
    }

}