package com.ca.mfd.prc.core.prm.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.utils.ArraysUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.dto.PrmDTO;
import com.ca.mfd.prc.core.prm.dto.PrmDetailDTO;
import com.ca.mfd.prc.core.prm.dto.TokenPermissionDTO;
import com.ca.mfd.prc.core.prm.dto.TreeItem;
import com.ca.mfd.prc.core.prm.entity.PrmInterfacePermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRolePermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmTokenEntity;
import com.ca.mfd.prc.core.prm.entity.PrmTokenPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserPermissionEntity;
import com.ca.mfd.prc.core.prm.service.IPrmInterfacePermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmRolePermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmRoleService;
import com.ca.mfd.prc.core.prm.service.IPrmTokenPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmTokenService;
import com.ca.mfd.prc.core.prm.service.IPrmUserPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
import com.ca.mfd.prc.core.main.dto.MenuAllInfo;
import com.ca.mfd.prc.core.main.dto.SysMenuItemPrmInfo;
import com.ca.mfd.prc.core.main.entity.SysMenuItemEntity;
import com.ca.mfd.prc.core.main.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 用户授权
 *
 * @author eric.zhou
 * @date 2023-07-21
 */
@RestController
@RequestMapping("ucenter/prm")
@Tag(name = "用户授权")
public class PrmController extends BaseApiController {

    @Autowired
    IPrmRolePermissionService prmRolePermissionService;
    @Autowired
    private IPrmRoleService prmRoleService;
    @Autowired
    private IPrmUserService prmUserService;
    @Autowired
    private IPrmPermissionService prmPermissionService;
    @Autowired
    private IPrmUserPermissionService prmUserPermissionService;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private IPrmInterfacePermissionService prmInterfacePermissionService;
    @Autowired
    private IPrmTokenPermissionService prmTokenPermissionService;
    @Autowired
    private IPrmTokenService prmTokenService;

   /* @GetMapping(value = "/test")
    @Operation(summary = "当前权限用户临时权限绑定")
    public ResultVO test(String pid) {
        List<TokenPermissionDTO> tokenPermissions = prmTokenPermissionService.getTokenPermissions(pid);
        return new ResultVO<List<TokenPermissionDTO>>().ok(tokenPermissions);
    }*/

    @GetMapping(value = "/detail")
    @Operation(summary = "当前权限用户临时权限绑定")
    public ResultVO detail(Long id) {
        PrmPermissionEntity permission = prmPermissionService.getFirstById(id);
        if (permission == null) {
            throw new InkelinkException("权限码:" + permission.getPermissionCode() + ",没有找到相应的数据");
        }
        List<MenuAllInfo> menus = sysMenuService.getPermissionMenus(permission.getId());
        List<PrmDetailDTO.InterfacePermissionDTO> interfacePermission = prmInterfacePermissionService.getListByPrmPermissionid(permission.getId())
                .stream().map(c -> {
                    PrmDetailDTO.InterfacePermissionDTO et = new PrmDetailDTO.InterfacePermissionDTO();
                    et.setId(c.getId().toString());
                    et.setName(c.getPath());
                    et.setKey(c.getId().toString());
                    et.setTempId(c.getId().toString());
                    et.setTempKey(c.getId().toString());
                    et.setRemark(c.getRemark());
                    et.setPath(c.getPath());
                    et.setType(c.getType());
                    return et;
                }).collect(Collectors.toList());
        List<TokenPermissionDTO> tokenPermissions = prmTokenPermissionService.getTokenPermissions(permission.getId());

        PrmDetailDTO.PrmDetailResult data = new PrmDetailDTO.PrmDetailResult();
        PrmDetailDTO.PermissionDTO per = new PrmDetailDTO.PermissionDTO();
        per.setId(permission.getId().toString());
        per.setPermissionCode(permission.getPermissionCode());
        per.setPermissionName(permission.getPermissionName());
        per.setModel(permission.getModel());
        per.setRemark(permission.getRemark());
        data.setPermission(per);
        data.setMenus(menus);
        data.setInterfacePermission(interfacePermission);
        data.setTokenPermissions(tokenPermissions);

        ResultVO<PrmDetailDTO.PrmDetailResult> result = new ResultVO<>();
        result.setMessage("操作成功");
        return result.ok(data);
    }

    @GetMapping(value = "/permission/tree")
    @Operation(summary = "权限树")
    public ResultVO permissionTree(String keyword) {
        List<PrmPermissionEntity> permissions = prmPermissionService.getListByKey(keyword);
        List<TreeItem> prmList = new ArrayList();
        for (PrmPermissionEntity item : permissions) {
            String modelName = StringUtils.isBlank(item.getModel()) ? "默认" : item.getModel();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            TreeItem node = new TreeItem();
            node.setKey(item.getId().toString());
            node.setLabel(item.getPermissionName());
            node.setIsPrm(true);
            Map data = new LinkedHashMap();
            //MpSqlUtils.getColumnName(PrmUserEntity::getCode)
            data.put("id", item.getId().toString());
            data.put("permissionCode", item.getPermissionCode());
            data.put("permissionName", item.getPermissionName());
            data.put("model", item.getModel());
            data.put("remark", item.getRemark());
            data.put("recycleDt", "");
            node.setData(data);
            initNodeLa(prmList, modelList, 0, node);
        }
        ResultVO<List<TreeItem>> result = new ResultVO<>();
        return result.ok(prmList);
    }

    void initNodeLa(List<TreeItem> nodes, List<String> modelList, int index, TreeItem leafNode) {
        String name = modelList.get(index);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), name)).findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(name);
            Map data = new LinkedHashMap();
            data.put("id", Constant.DEFAULT_ID);
            data.put("model", path);
            node.setData(data);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLa(node.getChildren(), modelList, index, leafNode);
        } else {
            node.getChildren().add(leafNode);
        }
    }

    @PostMapping(value = "/permission/save")
    @Operation(summary = "权限保存")
    public ResultVO permissionSave(@RequestBody PrmDTO.PermissionModel model) {
        ResultVO result = new ResultVO<String>();
        result.setMessage("保存成功");
        if ((model.getId().equals(Constant.DEFAULT_ID.toString()))) {
            prmPermissionService.insert(model.getService());
        } else {
            UpdateWrapper<PrmPermissionEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PrmPermissionEntity::getPermissionCode, model.getPermissionCode())
                    .set(PrmPermissionEntity::getPermissionName, model.getPermissionName())
                    .set(PrmPermissionEntity::getModel, model.getModel())
                    .set(PrmPermissionEntity::getRemark, model.getDescription())
                    .eq(PrmPermissionEntity::getId, model.getId());
            prmPermissionService.update(upset);
        }

        prmPermissionService.saveChange();
        return result.ok(model);
    }

    @PostMapping(value = "/permission/del")
    @Operation(summary = "权限删除")
    public ResultVO permissionDel(@RequestBody IdsModel model) {
        prmPermissionService.delete(model.getIds());
        prmPermissionService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @GetMapping(value = "user/permission/tree")
    @Operation(summary = "根据用户ID获取权限树")
    public ResultVO userPermissionTree(String userId) {
        List<ConditionDto> prmUsrCon = new ArrayList<>();
        prmUsrCon.add(new ConditionDto(MpSqlUtils.getColumnName(PrmUserPermissionEntity::getPrcPrmUserId), userId, ConditionOper.Equal));
        List<PrmUserPermissionEntity> prmDatas = prmUserPermissionService.getData(prmUsrCon).stream().map(
                c -> {
                    PrmUserPermissionEntity et = new PrmUserPermissionEntity();
                    et.setPrcPrmPermissionId(c.getPrcPrmPermissionId());
                    et.setRecycleDt(c.getRecycleDt());
                    return et;
                }
        ).collect(Collectors.toList());

        List<SortDto> perOrder = new ArrayList<>();
        perOrder.add(new SortDto(MpSqlUtils.getColumnName(PrmPermissionEntity::getModel), ConditionDirection.ASC));
        List<PrmPermissionEntity> permissionAll = prmPermissionService.getData(null, perOrder, false);

        List<TreeItem> permissions = new ArrayList<>();

        for (PrmPermissionEntity item : permissionAll) {
            String modelName = StringUtils.isBlank(item.getModel()) ? "默认" : item.getModel();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            String label = modelName + "->" + item.getPermissionName();
            PrmUserPermissionEntity prm = prmDatas.stream().filter(w -> Objects.equals(w.getPrcPrmPermissionId(), item.getId()))
                    .findFirst().orElse(null);
            TreeItem node = new TreeItem();
            node.setKey(item.getId().toString());
            node.setLabel(label);
            node.setIsPrm(true);
            node.setIsSelected(prm != null);
            Map data = new LinkedHashMap();
            data.put("id", item.getId().toString());
            data.put("permissionCode", item.getPermissionCode());
            data.put("permissionName", item.getPermissionName());
            data.put("model", item.getModel());
            data.put("remark", item.getRemark());
            if (prm != null) {
                data.put("recycleDt", DateUtils.format(prm.getRecycleDt(), DateUtils.DATE_TIME_PATTERN));
            } else {
                data.put("recycleDt", null);
            }
            node.setData(data);
            initNodeLb(permissions, modelList, 0, node);
        }

        List<Map> selections = new ArrayList<>();
        selectionNodeLb(permissions, selections);

        ResultVO<Map> result = new ResultVO<>();
        Map resMp = new LinkedHashMap();
        resMp.put("permissions", permissions);
        resMp.put("selections", selections);
        return result.ok(resMp);
    }

    void selectionNodeLb(List<TreeItem> permissions, List<Map> selections) {
        for (TreeItem item : permissions) {
            if (item.getIsSelected()) {
                Map mp = new HashMap(10);
                mp.put("icon", item.getIcon());
                mp.put("key", item.getKey());
                mp.put("isPrm", item.getIsPrm());
                mp.put("data", item.getData());
                selections.add(mp);
            }
            if (item.getChildren().size() > 0) {
                selectionNodeLb(item.getChildren(), selections);
            }
        }
    }

    void initNodeLb(List<TreeItem> nodes, List<String> modelList, int index, TreeItem leafNode) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        String label = sb.toString();
        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), label))
                .findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(label);
            Map mp = new LinkedHashMap();
            mp.put("id", Constant.DEFAULT_ID);
            mp.put("model", path);
            node.setData(mp);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLb(node.getChildren(), modelList, index, leafNode);
        } else {
            node.getChildren().add(leafNode);
        }
    }


    @GetMapping(value = "user/tree")
    @Operation(summary = "获取用户树")
    public ResultVO userTree(String keyword) {
        List<PrmUserEntity> users = prmUserService.getListByKey(keyword).stream().map(c -> {
            PrmUserEntity et = new PrmUserEntity();
            et.setId(c.getId());
            et.setJobNo(c.getJobNo());
            et.setUserName(c.getUserName());
            et.setNickName(c.getNickName());
            et.setRemark(c.getRemark());
            et.setUserType(c.getUserType());
            et.setIsActive(c.getIsActive());
            return et;
        }).collect(Collectors.toList());


        List<TreeItem> prmList = new ArrayList<>();
        for (PrmUserEntity item : users) {
            String modelName = StringUtils.isBlank(item.getRemark()) ? "默认" : item.getRemark();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            TreeItem node = new TreeItem();
            node.setKey(item.getId().toString());
            node.setLabel(item.getNickName() + "/" + item.getUserName());
            node.setIsPrm(true);
            Map mp = new LinkedHashMap();
            mp.put("id", item.getId().toString());
            mp.put("code", item.getJobNo());
            mp.put("userName", item.getUserName());
            mp.put("nickName", item.getNickName());
            mp.put("remark", item.getRemark());
            mp.put("userType", item.getUserType());
            mp.put("isActive", item.getIsActive());
            node.setData(mp);

            InitNodeLc(prmList, modelList, 0, node);
        }

        ResultVO<List<TreeItem>> result = new ResultVO<>();
        return result.ok(prmList);
    }

    void InitNodeLc(List<TreeItem> nodes, List<String> modelList, int index, TreeItem leafNode) {
        String name = modelList.get(index);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), name))
                .findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(name);
            Map mp = new LinkedHashMap();
            mp.put("id", Constant.DEFAULT_ID);
            mp.put("remark", path);
            node.setData(mp);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            InitNodeLc(node.getChildren(), modelList, index, leafNode);
        } else {
            node.getChildren().add(leafNode);
        }
    }

    @PostMapping(value = "user/save")
    @Operation(summary = "用户临时权限绑定")
    public ResultVO userSave(@RequestBody PrmDTO.UserPrmModel model) {
        Map<String, Date> datas = new HashMap<>(model.getDatas().size());
        for (PrmDTO.UserPrmDataModel item : model.getDatas()) {
            if (UUIDUtils.isGuidEmpty(item.getPrmId()) || item.getRecycleDt() == null) {
                continue;
            }
            if (!datas.containsKey(item.getPrmId())) {
                datas.put(item.getPrmId(), item.getRecycleDt());
            }
        }
        prmUserPermissionService.userSave(model.getId(), datas);
        prmUserPermissionService.saveChange();

        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping(value = "user/del")
    @Operation(summary = "用户临时权限绑定")
    public ResultVO userDel(@RequestBody IdsModel model) {
        prmUserPermissionService.delete(model.getIds());
        prmUserPermissionService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }


    @GetMapping(value = "role/tree")
    @Operation(summary = "获取角色树")
    public ResultVO roleTree(String keyword) {
        List<PrmRoleEntity> roles = prmRoleService.getListByKey(keyword);
        List<TreeItem> prmList = new ArrayList<>();
        for (PrmRoleEntity item : roles) {
            String modelName = StringUtils.isBlank(item.getGroupName()) ? "默认" : item.getGroupName();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            TreeItem node = new TreeItem();
            node.setKey(item.getId().toString());
            node.setLabel(item.getRoleName());
            node.setIsPrm(true);
            Map mp = new HashMap(10);
            mp.put("id", item.getId().toString());
            mp.put("roleName", item.getRoleName());
            mp.put("groupName", item.getGroupName());
            mp.put("description", item.getRemark());
            node.setData(mp);

            initNodeLd(prmList, modelList, 0, node);
        }

        ResultVO<List<TreeItem>> result = new ResultVO<>();
        return result.ok(prmList);
    }

    void initNodeLd(List<TreeItem> nodes, List<String> modelList, int index, TreeItem leafNode) {
        String name = modelList.get(index);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), name))
                .findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(name);
            Map mp = new LinkedHashMap();
            mp.put("id", Constant.DEFAULT_ID);
            mp.put("groupName", path);
            node.setData(mp);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLd(node.getChildren(), modelList, index, leafNode);
        } else {
            node.getChildren().add(leafNode);
        }
    }


    @GetMapping(value = "role/permission/tree")
    @Operation(summary = "获取权限树")
    public ResultVO rolePermissionTree(String roleId) {
        PrmRoleEntity role = prmRoleService.get(roleId);
        List<ConditionDto> rolePrmCon = new ArrayList<>();
        rolePrmCon.add(new ConditionDto(MpSqlUtils.getColumnName(PrmRolePermissionEntity::getPrcPrmRoleId)
                , roleId, ConditionOper.Equal));
        List<PrmRolePermissionEntity> rolePermissions = prmRolePermissionService.getData(rolePrmCon).stream().map(c -> {
            PrmRolePermissionEntity et = new PrmRolePermissionEntity();
            et.setId(c.getId());
            et.setPrcPrmPermissionId(c.getPrcPrmPermissionId());
            return et;
        }).collect(Collectors.toList());

        List<SortDto> perOrders = new ArrayList<>();
        perOrders.add(new SortDto(MpSqlUtils.getColumnName(PrmPermissionEntity::getModel), ConditionDirection.ASC));
        List<PrmPermissionEntity> permissionAll = prmPermissionService.getData(null, perOrders, false);

        List<TreeItem> permissions = new ArrayList<>();
        for (PrmPermissionEntity item : permissionAll) {
            String modelName = StringUtils.isBlank(item.getModel()) ? "默认" : item.getModel();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");
            String label = modelName + "->" + item.getPermissionName();
            PrmRolePermissionEntity prm = rolePermissions.stream().filter(w ->
                            Objects.equals(w.getPrcPrmPermissionId(), item.getId()))
                    .findFirst().orElse(null);

            TreeItem node = new TreeItem();
            node.setKey(item.getId().toString());
            node.setLabel(label);
            node.setIsPrm(true);
            node.setIsSelected(prm != null);
            Map mp = new LinkedHashMap();
            mp.put("id", item.getId().toString());
            mp.put("permissionCode", item.getPermissionCode());
            mp.put("permissionName", item.getPermissionName());
            mp.put("model", item.getModel());
            mp.put("remark", item.getRemark());
            mp.put("recycleDt", "");
            node.setData(mp);

            initNodeLe(permissions, modelList, 0, node);
        }

        List<Map> selections = new ArrayList<>();
        selectionNodeLe(permissions, selections);

        Map resMp = new HashMap(10);
        resMp.put("role", role);
        resMp.put("permissions", permissions);
        resMp.put("selections", selections);
        return new ResultVO<Map>().ok(resMp);
    }

    void initNodeLe(List<TreeItem> nodes, List<String> modelList, int index, TreeItem leafNode) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        String label = sb.toString();
        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), label))
                .findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(label);
            Map mp = new LinkedHashMap();
            mp.put("id", Constant.DEFAULT_ID);
            mp.put("model", path);
            node.setData(mp);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLe(node.getChildren(), modelList, index, leafNode);
        } else {
            node.getChildren().add(leafNode);
        }
    }

    void selectionNodeLe(List<TreeItem> permissions, List<Map> selections) {
        for (TreeItem item : permissions) {
            if (item.getIsSelected()) {
                Map mp = new HashMap(10);
                mp.put("icon", item.getIcon());
                mp.put("key", item.getKey());
                mp.put("isPrm", item.getIsPrm());
                mp.put("data", item.getData());
                selections.add(mp);
            }
            if (item.getChildren().size() > 0) {
                selectionNodeLe(item.getChildren(), selections);
            }
        }
    }

    @PostMapping(value = "role/save")
    @Operation(summary = "角色权限绑定")
    public ResultVO roleSave(@RequestBody PrmDTO.RolePrmModel model) {
        prmRoleService.roleSave(model.getService(), model.getPermissions());
        prmRoleService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping(value = "role/del")
    @Operation(summary = "角色权限删除绑定")
    public ResultVO roleDel(@RequestBody IdsModel model) {
        prmRolePermissionService.delete(model.getIds());
        prmRolePermissionService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @GetMapping(value = "token/tree")
    @Operation(summary = "获取TOKEN树")
    public ResultVO tokenTree(String keyword) {
        List<PrmTokenEntity> tokens = prmTokenService.getListByKey(keyword);

        List<TreeItem> prmList = new ArrayList<>();
        for (PrmTokenEntity item : tokens) {
            String modelName = StringUtils.isBlank(item.getGroupName()) ? "默认" : item.getGroupName();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            TreeItem node = new TreeItem();
            node.setKey(item.getId().toString());
            node.setLabel(item.getTokenName());
            node.setIsPrm(true);
            Map mp = new LinkedHashMap();
            mp.put("id", item.getId().toString());
            mp.put("tokenName", item.getTokenName());
            mp.put("groupName", item.getGroupName());
            mp.put("tokenEnable", item.getTokenEnable());
            mp.put("token", item.getToken());
            mp.put("remark", item.getRemark());
            mp.put("expireDt", DateUtils.format(item.getExpireDt(), DateUtils.DATE_TIME_PATTERN));
            node.setData(mp);

            initNodeLf(prmList, modelList, 0, node);
        }

        return new ResultVO<List<TreeItem>>().ok(prmList);
    }


    void initNodeLf(List<TreeItem> nodes, List<String> modelList, int index, TreeItem leafNode) {
        String name = modelList.get(index);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), name))
                .findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(name);
            Map mp = new LinkedHashMap();
            mp.put("id", Constant.DEFAULT_ID);
            mp.put("groupName", path);
            node.setData(mp);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLf(node.getChildren(), modelList, index, leafNode);
        } else {
            node.getChildren().add(leafNode);
        }
    }

    @GetMapping(value = "token/permission/tree")
    @Operation(summary = "根据权限获取TOKEN树")
    public ResultVO tokenPermissionTree(Long id) {
        PrmPermissionEntity permission = prmPermissionService.getFirstById(id);
        if (permission == null) {
            throw new InkelinkException("权限码:" + permission.getPermissionCode() + ",没有找到相应的数据");
        }
        List<ConditionDto> tokenPreCons = new ArrayList<>();
        tokenPreCons.add(new ConditionDto(MpSqlUtils.getColumnName(PrmTokenPermissionEntity::getPrcPrmPermissionId)
                , permission.getId().toString(), ConditionOper.Equal));

        List<PrmTokenPermissionEntity> tokenPermissions = prmTokenPermissionService.getData(tokenPreCons)
                .stream().map(c -> {
                    PrmTokenPermissionEntity et = new PrmTokenPermissionEntity();
                    et.setId(c.getId());
                    et.setPrcPrmTokenId(c.getPrcPrmTokenId());
                    return et;
                }).collect(Collectors.toList());

        List<SortDto> prmTokenSorts = new ArrayList<>();
        prmTokenSorts.add(new SortDto(MpSqlUtils.getColumnName(PrmTokenEntity::getGroupName), ConditionDirection.ASC));
        List<PrmTokenEntity> permissionAll = prmTokenService.getData(null, prmTokenSorts, false);

        List<TreeItem> permissions = new ArrayList<>();
        for (PrmTokenEntity item : permissionAll) {
            String modelName = StringUtils.isBlank(item.getGroupName()) ? "默认" : item.getGroupName();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            String label = modelName + "->" + item.getTokenName();

            PrmTokenPermissionEntity prm = tokenPermissions.stream().filter(w ->
                            Objects.equals(w.getPrcPrmTokenId(), item.getId()))
                    .findFirst().orElse(null);

            TreeItem node = new TreeItem();
            node.setKey(item.getId().toString());
            node.setLabel(label);
            node.setIsPrm(true);
            node.setIsSelected(prm != null);
            Map mp = new LinkedHashMap();
            mp.put("id", item.getId().toString());
            mp.put("tokenName", item.getTokenName());
            mp.put("groupName", item.getGroupName());
            mp.put("tokenEnable", item.getTokenEnable());
            mp.put("token", item.getToken());
            mp.put("remark", item.getRemark());
            mp.put("expireDt", item.getExpireDt());
            node.setData(mp);
            initNodeLg(permissions, modelList, 0, node);
        }

        List<Map> selections = new ArrayList<>();
        selectionNodeLg(permissions, selections);

        Map resMp = new HashMap(2);
        resMp.put("permissions", permissions);
        resMp.put("selections", selections);
        return new ResultVO<Map>().ok(resMp);
    }

    void initNodeLg(List<TreeItem> nodes, List<String> modelList, int index, TreeItem leafNode) {
        String name = modelList.get(index);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), name))
                .findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(name);
            Map mp = new LinkedHashMap();
            mp.put("id", Constant.DEFAULT_ID);
            mp.put("groupName", path);
            node.setData(mp);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLg(node.getChildren(), modelList, index, leafNode);
        } else {
            node.getChildren().add(leafNode);
        }
    }

    void selectionNodeLg(List<TreeItem> permissions, List<Map> selections) {
        for (TreeItem item : permissions) {
            if (item.getIsSelected()) {
                Map mp = new LinkedHashMap();
                mp.put("icon", item.getIcon());
                mp.put("key", item.getKey());
                mp.put("isPrm", item.getIsPrm());
                mp.put("data", item.getData());
                selections.add(mp);
            }
            if (item.getChildren().size() > 0) {
                selectionNodeLg(item.getChildren(), selections);
            }
        }
    }

    @PostMapping(value = "token/save")
    @Operation(summary = "TOKEN权限绑定")
    public ResultVO tokenSave(@RequestBody PrmDTO.TokenPrmModel model) {
        if (model.getDatas() == null) {
            model.setDatas(new ArrayList<>());
        }
        prmTokenPermissionService.permissionSave(model.getId(), model.getDatas());
        prmTokenPermissionService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping(value = "token/del")
    @Operation(summary = "TOKEN权限删除绑定")
    public ResultVO tokenDel(@RequestBody IdsModel model) {
        prmTokenPermissionService.delete(model.getIds());
        prmTokenPermissionService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @GetMapping(value = "interface/tree")
    @Operation(summary = "获取接口树")
    public ResultVO interfaceTree(String keyword) {
        List<PrmInterfacePermissionEntity> roles = prmInterfacePermissionService.getListByKey(keyword);
        List<TreeItem> prmList = new ArrayList<>();
        for (PrmInterfacePermissionEntity item : roles) {
            String modelName = StringUtils.isBlank(item.getRemark()) ? "默认" : item.getRemark();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            TreeItem node = new TreeItem();
            node.setKey(item.getId().toString());
            node.setLabel(item.getPath());
            node.setIsPrm(true);
            Map mp = new LinkedHashMap();
            mp.put("id", item.getId().toString());
            mp.put("path", item.getPath());
            mp.put("type", item.getType());
            mp.put("remark", item.getRemark());
            node.setData(mp);
            initNodeLh(prmList, modelList, 0, node);
        }

        return new ResultVO<List<TreeItem>>().ok(prmList);

    }

    void initNodeLh(List<TreeItem> nodes, List<String> modelList, int index, TreeItem leafNode) {
        String name = modelList.get(index);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), name))
                .findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(name);
            Map rmp = new HashMap(5);
            rmp.put("id", Constant.DEFAULT_ID);
            rmp.put("remark", path);
            node.setData(rmp);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLh(node.getChildren(), modelList, index, leafNode);
        } else {
            node.getChildren().add(leafNode);
        }
    }

    @GetMapping(value = "interface/permission/tree")
    @Operation(summary = "获取接口树")
    public ResultVO interfacePermissionTree(Long id) {
        PrmPermissionEntity permission = prmPermissionService.getFirstById(id);
        if (permission == null) {
            throw new InkelinkException("权限码:" + permission.getPermissionCode() + ",没有找到相应的数据");
        }
        List<Serializable> interfacePermission = prmInterfacePermissionService
                .getListByPrmPermissionid(permission.getId())
                .stream().map(c -> c.getId()).collect(Collectors.toList());
        List<PrmInterfacePermissionEntity> permissionAll = prmInterfacePermissionService.getData(null);
        permissionAll.sort(Comparator.comparing(c -> c.getRemark()));

        List<TreeItem> permissions = new ArrayList<>();
        for (PrmInterfacePermissionEntity item : permissionAll) {
            String modelName = StringUtils.isBlank(item.getRemark()) ? "默认" : item.getRemark();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            String label = modelName + "->" + item.getPath();
            Serializable prm = interfacePermission.stream().filter(w ->
                            StringUtils.equals(w.toString(), item.getId().toString()))
                    .findFirst().orElse(null);

            TreeItem node = new TreeItem();
            node.setKey(item.getId().toString());
            node.setLabel(item.getPath());
            node.setIsPrm(true);
            node.setIsSelected(prm != null);
            Map mp = new LinkedHashMap();
            mp.put("id", item.getId().toString());
            mp.put("path", item.getPath());
            mp.put("type", item.getType());
            mp.put("remark", item.getRemark());
            node.setData(mp);

            initNodeLi(permissions, modelList, 0, node);
        }

        List<Map> selections = new ArrayList<>();
        selectionNodeli(permissions, selections);

        Map resMp = new LinkedHashMap();
        resMp.put("permissions", permissions);
        resMp.put("selections", selections);

        return new ResultVO<Map>().ok(resMp);


    }

    void initNodeLi(List<TreeItem> nodes, List<String> modelList, int index, TreeItem leafNode) {
        String name = modelList.get(index);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), name))
                .findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(name);
            Map mp = new LinkedHashMap();
            mp.put("id", Constant.DEFAULT_ID);
            mp.put("remark", path);
            node.setData(mp);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLi(node.getChildren(), modelList, index, leafNode);
        } else {
            node.getChildren().add(leafNode);
        }
    }

    void selectionNodeli(List<TreeItem> permissions, List<Map> selections) {
        for (TreeItem item : permissions) {
            if (item.getIsSelected()) {
                Map mp = new LinkedHashMap();
                mp.put("icon", item.getIcon());
                mp.put("key", item.getKey());
                mp.put("isPrm", item.getIsPrm());
                mp.put("data", item.getData());
                selections.add(mp);
            }
            if (item.getChildren().size() > 0) {
                selectionNodeli(item.getChildren(), selections);
            }
        }
    }

    @PostMapping(value = "interface/save")
    @Operation(summary = "接口权限绑定")
    public ResultVO interfaceSave(@RequestBody PrmDTO.InterfacePrmModel model) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("操作成功");

        String id = model.getId();
        if (model.getDatas() == null) {
            model.setDatas(new ArrayList<>());
        }
        List<String> datas = model.getDatas();

        datas = datas.stream().distinct().collect(Collectors.toList());

        List<PrmInterfacePermissionEntity> list = prmInterfacePermissionService.getListByPrmPermissionid(id);

        for (String item : datas) {
            if (UUIDUtils.isGuidEmpty(item)) {
                continue;
            }
            PrmInterfacePermissionEntity data = list.stream().filter(w -> StringUtils.equals(item, w.getId().toString()))
                    .findFirst().orElse(null);
            if (data == null) {
                UpdateWrapper<PrmInterfacePermissionEntity> upset = new UpdateWrapper<>();
                upset.lambda().set(PrmInterfacePermissionEntity::getPrcPrmPermissionId, id)
                        .eq(PrmInterfacePermissionEntity::getId, item);
                prmInterfacePermissionService.update(upset);
            } else {
                list.remove(data);
            }
        }
        if (list.size() > 0) {
            List<Serializable> ids = list.stream().map(w -> w.getId()).collect(Collectors.toList());
            UpdateWrapper<PrmInterfacePermissionEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PrmInterfacePermissionEntity::getPrcPrmPermissionId, UUIDUtils.getEmpty())
                    .in(PrmInterfacePermissionEntity::getId, ids);
            prmInterfacePermissionService.update(upset);
        }
        prmRolePermissionService.saveChange();
        return result.ok("");
    }

    @PostMapping(value = "interface/del")
    @Operation(summary = "接口权限删除绑定")
    public ResultVO interfaceDel(@RequestBody IdsModel model) {
        if (model.getIds() != null && model.getIds().length > 0) {
            String[] ids = model.getIds();
            UpdateWrapper<PrmInterfacePermissionEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PrmInterfacePermissionEntity::getPrcPrmPermissionId, Constant.DEFAULT_ID)
                    .in(PrmInterfacePermissionEntity::getId, ids);
            prmInterfacePermissionService.update(upset);
            prmInterfacePermissionService.saveChange();
        }
        return new ResultVO<String>().ok("", "操作成功");
    }

    @GetMapping(value = "menu/tree")
    @Operation(summary = "根据菜单ID菜单项数据")
    public ResultVO menuTree(String menuId) {
        List<SysMenuItemEntity> menuItems = sysMenuService.getMenuItems(menuId);
        List<TreeItem> nodes = new ArrayList<>();
        //一级菜单
        List<SysMenuItemEntity> rootMenuItmes = menuItems.stream().filter(o ->
                o.getPosition().split("\\.").length == 1
                        && !o.getIsHide()).collect(Collectors.toList());
        for (SysMenuItemEntity menuItem : rootMenuItmes) {
            TreeItem node = initNodeLj(menuItems, menuItem);
            if (node != null) {
                nodes.add(node);
            }
        }
        return new ResultVO<List<TreeItem>>().ok(nodes);


    }

    TreeItem initNodeLj(List<SysMenuItemEntity> menuItems, SysMenuItemEntity menuItem) {
        TreeItem node = new TreeItem();
        node.setKey(menuItem.getId().toString());
        node.setLabel(menuItem.getItemName());
        node.setIcon(menuItem.getIcon());
        node.setIsPrm(true);
        Map mp = new LinkedHashMap();
        mp.put("id", menuItem.getId().toString());
        mp.put("openType", menuItem.getOpenType());
        mp.put("icon", menuItem.getIcon());
        mp.put("isHide", menuItem.getIsHide());
        mp.put("name", menuItem.getItemName());
        mp.put("position", menuItem.getPosition());
        mp.put("url", menuItem.getUrl());
        node.setData(mp);
        List<SysMenuItemEntity> childMenuItems = menuItems.stream().filter(o ->
                o.getPosition().split("\\.").length ==
                        (menuItem.getPosition().split("\\.").length + 1)
                        && o.getPosition().startsWith(menuItem.getPosition() + ".")
                        && !o.getIsHide()).collect(Collectors.toList());

        if (childMenuItems.size() > 0) {
            for (SysMenuItemEntity childMenuItem : childMenuItems) {
                TreeItem childNode = initNodeLj(menuItems, childMenuItem);
                if (childNode != null) {
                    node.getChildren().add(childNode);
                }
            }
        }
        return node;
    }

    @GetMapping(value = "menu/permission/tree")
    @Operation(summary = "获取菜单权限树")
    public ResultVO menuPermissionTree(String menuId, Long id) {
        PrmPermissionEntity permission = prmPermissionService.getFirstById(id);
        if (permission == null) {
            throw new InkelinkException("权限码:" + permission.getPermissionCode() + ",没有找到相应的数据");
        }
        List<SysMenuItemPrmInfo> menus = sysMenuService.getPermissionMenuItems(Long.valueOf(menuId), permission.getId());

        List<SysMenuItemEntity> menuItems = sysMenuService.getMenuItems(menuId);
        List<TreeItem> permissions = new ArrayList<>();
        //一级菜单
        List<SysMenuItemEntity> rootMenuItmes = menuItems.stream().filter(o ->
                o.getPosition().split("\\.").length == 1
                        && !o.getIsHide()).collect(Collectors.toList());
        for (SysMenuItemEntity menuItem : rootMenuItmes) {
            TreeItem node = initNodeLk(menuItems, menuItem, menus);
            if (node != null) {
                permissions.add(node);
            }
        }
        List<Map> selections = new ArrayList<>();
        selectionNodeLk(permissions, selections);

        Map resMp = new LinkedHashMap();
        resMp.put("permissions", permissions);
        resMp.put("selections", selections);

        return new ResultVO<Map>().ok(resMp);
    }

    TreeItem initNodeLk(List<SysMenuItemEntity> menuItems, SysMenuItemEntity menuItem, List<SysMenuItemPrmInfo> menus) {
        String label = menuItem.getItemName();
        SysMenuItemPrmInfo prm = menus.stream().filter(w ->
                        StringUtils.equals(w.getPrcPrmMenuItemId().toString(), menuItem.getId().toString()))
                .findFirst().orElse(null);

        TreeItem node = new TreeItem();
        node.setKey(menuItem.getId().toString());
        node.setLabel(label);
        node.setIcon(menuItem.getIcon());
        node.setIsPrm(true);
        node.setIsSelected(prm != null);
        Map mp = new LinkedHashMap();
        mp.put("id", menuItem.getId().toString());
        mp.put("openType", menuItem.getOpenType());
        mp.put("icon", menuItem.getIcon());
        mp.put("isHide", menuItem.getIsHide());
        mp.put("name", menuItem.getItemName());
        mp.put("position", menuItem.getPosition());
        mp.put("url", menuItem.getUrl());
        node.setData(mp);

        List<SysMenuItemEntity> childMenuItems = menuItems.stream().filter(o ->
                o.getPosition().split("\\.").length
                        == (menuItem.getPosition().split("\\.").length + 1)
                        && o.getPosition().startsWith(menuItem.getPosition() + ".")
                        && !o.getIsHide()).collect(Collectors.toList());

        if (childMenuItems.size() > 0) {
            for (SysMenuItemEntity childMenuItem : childMenuItems) {
                //childMenuItem.Name = menuItem.Name + "->" + childMenuItem.Name;
                TreeItem childNode = initNodeLk(menuItems, childMenuItem, menus);
                if (childNode != null) {
                    node.getChildren().add(childNode);
                }
            }
        }
        return node;
    }

    void selectionNodeLk(List<TreeItem> permissions, List<Map> selections) {
        for (TreeItem item : permissions) {
            if (item.getIsSelected()) {
                Map mp = new LinkedHashMap();
                mp.put("icon", item.getIcon());
                mp.put("key", item.getKey());
                mp.put("isPrm", item.getIsPrm());
                mp.put("data", item.getData());
                selections.add(mp);
            }
            if (item.getChildren().size() > 0) {
                selectionNodeLk(item.getChildren(), selections);
            }
        }
    }

    @PostMapping(value = "menu/save")
    @Operation(summary = "菜单权限绑定")
    public ResultVO menuSave(@RequestBody PrmDTO.MenuPrmModel model) {
        if (model.getDatas() == null) {
            model.setDatas(new ArrayList<>());
        }
        sysMenuService.permissionSave(model.getId(), model.getMenuId(), model.getDatas());
        sysMenuService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping(value = "menu/del")
    @Operation(summary = "菜单权限删除绑定")
    public ResultVO menuDel(@RequestBody IdsModel model) {
        sysMenuService.permissionDel(new ArrayList<Serializable>(Arrays.asList(model.getIds())));
        sysMenuService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping(value = "clearbad")
    @Operation(summary = "清理无效的绑定")
    public ResultVO clearBad() {
        List<String> ids = prmUserPermissionService.getUserPermissionRemoves();

        if (ids != null && ids.size() > 0) {
            prmUserPermissionService.delete(ids.toArray(new String[0]));
        }

        ids = prmRolePermissionService.getRolePermissionRemoves();
        if (ids != null && ids.size() > 0) {
            prmRolePermissionService.delete(ids.toArray(new String[0]));
            prmRolePermissionService.saveChange();
        }
        ids = prmTokenPermissionService.getTokenPermissionRemoves();
        if (ids != null && ids.size() > 0) {
            prmTokenPermissionService.delete(ids.toArray(new String[0]));
            prmTokenPermissionService.saveChange();
        }
        ids = prmInterfacePermissionService.getInterfacePermissionRemoves();
        if (ids != null && ids.size() > 0) {
            UpdateWrapper<PrmInterfacePermissionEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PrmInterfacePermissionEntity::getPrcPrmPermissionId, UUIDUtils.getEmpty())
                    .in(PrmInterfacePermissionEntity::getId, ids);
            prmInterfacePermissionService.update(upset);
            prmInterfacePermissionService.saveChange();
        }
        sysMenuService.permissionClear();
        sysMenuService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

}