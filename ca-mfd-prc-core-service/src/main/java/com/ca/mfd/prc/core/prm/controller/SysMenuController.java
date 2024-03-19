package com.ca.mfd.prc.core.prm.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.core.prm.dto.MenuNodeInfo;
import com.ca.mfd.prc.core.main.dto.MenuInfo;
import com.ca.mfd.prc.core.main.dto.SaveMenu;
import com.ca.mfd.prc.core.main.dto.SaveMenuByPermissonsModel;
import com.ca.mfd.prc.core.main.entity.SysMenuEntity;
import com.ca.mfd.prc.core.main.entity.SysMenuItemEntity;
import com.ca.mfd.prc.core.main.entity.SysMenuItemVersionEntity;
import com.ca.mfd.prc.core.main.service.ISysMenuItemVersionService;
import com.ca.mfd.prc.core.main.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜单管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmmenu")
@Tag(name = "菜单管理")
public class SysMenuController extends BaseController<SysMenuEntity> {

    private static final Object obj = new Object();
    private final ISysMenuService sysMenuService;
    @Autowired
    private ISysMenuItemVersionService sysMenuItemVersionService;

    @Autowired
    private IdentityHelper identityHelper;

    @Autowired
    public SysMenuController(ISysMenuService sysMenuService) {
        this.crudService = sysMenuService;
        this.sysMenuService = sysMenuService;
    }

    @GetMapping(value = "getmenucombodatas")
    @Operation(summary = "获取菜单combo数据")
    public ResultVO getMenuComboDatas() {
        List<ComboInfoDTO> data = sysMenuService.getData(null).stream().map(o -> {
            ComboInfoDTO cmb = new ComboInfoDTO();
            cmb.setText(o.getMenuName());
            cmb.setValue(o.getId().toString());
            return cmb;
        }).sorted(Comparator.comparing(ComboInfoDTO::getText)).collect(Collectors.toList());
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @GetMapping(value = "getmenuinfodatas")
    @Operation(summary = "获取菜单")
    public ResultVO getMenuInfoDatas() {
        List<SysMenuEntity> data = sysMenuService.getData(null);
        data.sort(Comparator.comparing(SysMenuEntity::getMenuName));
        ResultVO<List<SysMenuEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @GetMapping(value = "getmenuitems")
    @Operation(summary = "获取菜单项")
    public ResultVO getMenuItems(String menuId) {
        List<SysMenuItemEntity> datas = sysMenuService.getMenuItems(menuId, true);
        List<MenuInfo> data = getMenuItemNewTreeNodes("", datas, 1);
        ResultVO<List<MenuInfo>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @GetMapping(value = "getviewmenuitems")
    @Operation(summary = "预览版本查看")
    public ResultVO getViewMenuItems(String menuId) {
        List<MenuInfo> datas = JsonUtils.parseArray(sysMenuItemVersionService.get(menuId).getMenuItemTree(), MenuInfo.class);
        ResultVO<List<MenuInfo>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(datas);
    }

    @GetMapping(value = "geteasyuimenu")
    @Operation(summary = "根据菜单名称获取EasyUi规定格式的菜单")
    public ResultVO getEasyUiMenu(String menuName) {
        List<MenuNodeInfo> data = sysMenuService.getMenuTree(menuName);
        ResultVO<List<MenuNodeInfo>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @GetMapping(value = "getmyeasyuimenu")
    @Operation(summary = "获取EasyUi规定格式的菜单")
    public ResultVO getMyEasyUiMenu() {
        //需要读取系统配置 inkelink.User.SysMenu
        Integer usrType = identityHelper.getLoginUser().getUserType();
        String menuName = SpringContextUtils.getConfiguration("User.SysMenu" + usrType, "AdminMenu");
        //("User.SysMenu{IdentityHelper.OnlineUser.UserInfo.UserType}", "AdminMenu");
        List<MenuNodeInfo> data = sysMenuService.getMenuTree(menuName);
        ResultVO<List<MenuNodeInfo>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @GetMapping(value = "getmenutree")
    @Operation(summary = "获取菜单树")
    public ResultVO getMenuTree(String menuName) {
        List<MenuNodeInfo> data = sysMenuService.getMenuTree(menuName);
        ResultVO<List<MenuNodeInfo>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @PostMapping(value = "save")
    @Operation(summary = "保存菜单数据")
    public ResultVO save(@RequestBody SaveMenu saveMenu) {
        //保证当前自有一个访问项
        synchronized (obj) {
            if (saveMenu.getMenuId() == null || saveMenu.getMenuId().toString().equals(Constant.DEFAULT_ID.toString())) {
                throw new InkelinkException("菜单编号不存在");
            }
            SysMenuEntity menuModel = sysMenuService.getMenuByFirst(saveMenu.getMenuId(), saveMenu.getVersion());
            if (menuModel == null) {
                throw new InkelinkException("当前菜单版本已被修改,请刷新界面重新操作");
            }
            ResultVO<List<SysMenuItemEntity>> result = new ResultVO<>();
            result.setMessage("保存成功");

            try {
                saveMenu.setDatas(saveMenu.getDatas().replace("00000000-0000-0000-0000-000000000000", "0"));
                List<SysMenuItemEntity> SysMenuItemEntitys = JsonUtils.parseArray(saveMenu.getDatas(), SysMenuItemEntity.class);
                sysMenuService.save(SysMenuItemEntitys, saveMenu.getMenuId());
                //更改
                UpdateWrapper<SysMenuEntity> upset = new UpdateWrapper<>();
                upset.lambda().set(SysMenuEntity::getVersion, saveMenu.getVersion() + 1)
                        .eq(SysMenuEntity::getId, saveMenu.getMenuId());
                sysMenuService.update(upset);
                //添加版本管理
                sysMenuService.saveChange();
                try {
                    List<SysMenuItemEntity> datas = sysMenuService.getMenuItems(saveMenu.getMenuId(), true);
                    List<MenuInfo> data = getMenuItemNewTreeNodes("", datas, 1);
                    SysMenuItemVersionEntity menuItemVersion = new SysMenuItemVersionEntity();
                    menuItemVersion.setPrcPrmMenuId(Long.valueOf(saveMenu.getMenuId().toString()));
                    menuItemVersion.setMenuName(menuModel.getMenuName());
                    menuItemVersion.setVersion(saveMenu.getVersion() + 1);
                    menuItemVersion.setMenuItemContent(saveMenu.getDatas());
                    menuItemVersion.setMenuItemTree(JsonUtils.toJsonString(data));
                    sysMenuItemVersionService.insert(menuItemVersion);
                    sysMenuItemVersionService.saveChange();
                } catch (Exception exe) {
                    result.setCode(-1);
                    result.setMessage("版本保存失败");
                }

            } catch (InkelinkException ex) {
                result.setCode(-1);
                result.setMessage(ex.getMessage());
            } catch (Exception ex) {
                throw new InkelinkException("保存失败", ex);
            }
            return result.ok(null);
        }
    }

    private List<SysMenuItemEntity> getMenuItemTreeNodes(String position, List<SysMenuItemEntity> datas, int deep) {
        int finalDeep = deep;
        List<SysMenuItemEntity> nodes = datas.stream().filter(o -> o.getPosition().split("\\.").length == finalDeep
                && o.getPosition().startsWith(position)).collect(Collectors.toList());
        deep++;
        for (SysMenuItemEntity node : nodes) {
            List<SysMenuItemEntity> children = getMenuItemTreeNodes(node.getPosition() + ".", datas, deep);
            if (children.size() > 0) {
                node.children = children;
            }
        }
        return nodes;
    }


    @GetMapping(value = "backmenum")
    @Operation(summary = "获取菜单项关联的权限")
    public ResultVO backMenum(String id) {
        SysMenuItemVersionEntity menuItemVersion = sysMenuItemVersionService.get(id);
        if (menuItemVersion.getIsDelete()) {
            menuItemVersion = null;
        }
        if (menuItemVersion == null) {
            throw new InkelinkException("回滚版本不存在");
        }
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("回滚成功");
        try {
            List<SysMenuItemEntity> SysMenuItemEntitys = JsonUtils.parseArray(menuItemVersion.getMenuItemContent(), SysMenuItemEntity.class);
            sysMenuService.backSave(SysMenuItemEntitys, menuItemVersion.getPrcPrmMenuId());
            //更改
            UpdateWrapper<SysMenuEntity> upset = new UpdateWrapper<>();
            upset.lambda()
                    .set(SysMenuEntity::getVersion, menuItemVersion.getVersion())
                    .eq(SysMenuEntity::getId, menuItemVersion.getPrcPrmMenuId());
            sysMenuService.update(upset);
            sysMenuService.saveChange();
        } catch (Exception ex) {
            result.setCode(-1);
            result.setMessage(ex.getMessage());
            return result;
        }
        //添加版本管理
        return result.ok("");
    }

    private List<MenuInfo> getMenuItemNewTreeNodes(String position, List<SysMenuItemEntity> datas, int deep) {
        int finalDeep = deep;
        List<MenuInfo> nodes = datas.stream().filter(o -> o.getPosition().split("\\.").length == finalDeep
                && o.getPosition().startsWith(position)).map(s -> {
                    MenuInfo met = new MenuInfo();
                    met.setId(s.getId());
                    met.setData(s);
                    met.setPosition(s.getPosition());
                    return met;
                }
        ).collect(Collectors.toList());
        deep++;
        for (MenuInfo node : nodes) {
            List<MenuInfo> children = getMenuItemNewTreeNodes(node.getPosition() + ".", datas, deep);
            if (children.size() > 0) {
                node.setChildren(children);
            }
        }
        return nodes;
    }

    @GetMapping(value = "/getmenupermissions")
    @Operation(summary = "获取菜单项关联的权限")
    public ResultVO getMenuPermissions(String menuItemId) {
        List<String> data = sysMenuService.getMenuItemPermissions(menuItemId);
        ResultVO<List<String>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @PostMapping(value = "/savepermissions")
    @Operation(summary = "保存菜单项关联的权限")
    public ResultVO savePermissions(@RequestBody SaveMenuByPermissonsModel saveMenuModel) {
        if (saveMenuModel.getPermissionIds() == null) {
            saveMenuModel.setPermissionIds(new ArrayList<>());
        }
        List<Serializable> permissionIds = saveMenuModel.getPermissionIds();
        sysMenuService.savePermissions(saveMenuModel.getMenuItemId(), permissionIds);
        sysMenuService.saveChange();
        ResultVO result = new ResultVO<>();
        result.setMessage("保存成功");
        return result.ok("");
    }

}