package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.comparer.FlatPositionComparer;
import com.ca.mfd.prc.common.dto.core.MenuItemsPremissmsInfo;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.prm.dto.MenuNodeInfo;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.main.dto.MenuAllInfo;
import com.ca.mfd.prc.core.main.dto.MenuPremissmsInfo;
import com.ca.mfd.prc.core.main.dto.SysMenuItemPrmInfo;
import com.ca.mfd.prc.core.main.entity.SysMenuEntity;
import com.ca.mfd.prc.core.main.entity.SysMenuItemEntity;
import com.ca.mfd.prc.core.main.entity.SysMenuPermissionEntity;
import com.ca.mfd.prc.core.main.mapper.ISysMenuMapper;
import com.ca.mfd.prc.core.main.service.IAuthorizationService;
import com.ca.mfd.prc.core.main.service.ILocalizationService;
import com.ca.mfd.prc.core.main.service.ISysMenuItemService;
import com.ca.mfd.prc.core.main.service.ISysMenuPermissionService;
import com.ca.mfd.prc.core.main.service.ISysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysMenuServiceImpl extends AbstractCrudServiceImpl<ISysMenuMapper, SysMenuEntity> implements ISysMenuService {

    @Autowired
    private ISysMenuMapper sysMenuDao;

    @Autowired
    private ISysMenuItemService sysMenuItemService;

    @Autowired
    private IAuthorizationService authorizationService;

    @Autowired
    private ISysMenuPermissionService sysMenuPermissionService;
    @Autowired
    private ILocalizationService localizationService;

    @Override
    public void beforeInsert(SysMenuEntity model) {
        validMenuData(model);
    }

    @Override
    public void beforeUpdate(SysMenuEntity model) {
        validMenuData(model);
    }

    private void validMenuData(SysMenuEntity model) {
        validDataUnique(model.getId(), "MENU_NAME", model.getMenuName(), "已经存在名称为%s的数据", "", "");
    }

    /**
     * 获取菜单
     *
     * @param menuId  菜单项ID
     * @param version 版本
     * @return 菜单项
     */
    @Override
    public SysMenuEntity getMenuByFirst(Serializable menuId, Integer version) {
        QueryWrapper<SysMenuEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(SysMenuEntity::getId, menuId)
                .eq(SysMenuEntity::getVersion, version);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 生成Position
     *
     * @param models         菜单项集合
     * @param parentPosition 菜单位置
     * @param datas          菜单项集合
     */
    private void generationPosition(List<SysMenuItemEntity> models, String parentPosition
            , List<SysMenuItemEntity> datas) {
        parentPosition = StringUtils.isBlank(parentPosition) ? "" : parentPosition + ".";
        int i = 1;
        for (SysMenuItemEntity model : models) {
            model.setPosition(parentPosition + i);
            i++;
            if (model.children != null && model.children.size() > 0) {
                generationPosition(model.children, model.getPosition(), datas);
            }
            datas.add(model);
        }
    }

    /**
     * 获取菜单关联的权限
     *
     * @param menuItemId 菜单项外键
     * @return 权限外键集合
     */
    @Override
    public List<String> getMenuItemPermissions(Serializable menuItemId) {
        List<String> permissionIds = new ArrayList();
        List<ConditionDto> conditions = new ArrayList();
        conditions.add(new ConditionDto("PRC_PRM_MENU_ITEM_ID", menuItemId.toString(), ConditionOper.Equal));
        conditions.add(new ConditionDto("IS_DELETE", "false", ConditionOper.Equal));

        permissionIds = sysMenuPermissionService.getData(conditions).stream().map(o -> o.getPrcPrmPermissionId().toString()).collect(Collectors.toList());
        return permissionIds;
    }

    /**
     * 保存菜单的权限
     *
     * @param menuItemId    菜单项外键
     * @param permissionIds 权限集合
     */
    @Override
    public void savePermissions(Serializable menuItemId, List<Serializable> permissionIds) {
        List<ConditionDto> conditions = new ArrayList();
        conditions.add(new ConditionDto("PRC_PRM_MENU_ITEM_ID", menuItemId.toString(), ConditionOper.Equal));
        conditions.add(new ConditionDto("IS_DELETE", "1", ConditionOper.Equal));

        //删除菜单项已经有的权限（物理删除）
        sysMenuPermissionService.delete(conditions, false);
        //重置权限
        for (Serializable permissionId : permissionIds) {
            SysMenuPermissionEntity et = new SysMenuPermissionEntity();
            et.setPrcPrmPermissionId(Long.valueOf((String) permissionId));
            et.setPrcPrmMenuItemId(Long.valueOf((String) menuItemId));
            sysMenuPermissionService.insert(et);
        }
    }

    /**
     * 保存菜单的权限
     *
     * @param prmId       权限外键
     * @param menuId      菜单外键
     * @param menuItemIds 菜单项集合
     */
    @Override
    public void permissionSave(Serializable prmId, Serializable menuId, List<Serializable> menuItemIds) {
        if (menuItemIds == null) {
            menuItemIds = new ArrayList<>();
        }
        menuItemIds = menuItemIds.stream().distinct().collect(Collectors.toList());
        List<SysMenuPermissionEntity> permissions = sysMenuDao.getSysMenuPermissions(prmId, menuId);
        //重置权限
        for (Serializable menuItemId : menuItemIds) {
            if (menuItemId == null) {
                continue;
            }
            SysMenuPermissionEntity item = permissions.stream().filter(w -> Objects.equals(w.getPrcPrmMenuItemId(), menuItemId))
                    .findFirst().orElse(null);
            if (item == null) {
                SysMenuPermissionEntity et = new SysMenuPermissionEntity();
                et.setPrcPrmPermissionId(Long.valueOf((String) prmId));
                et.setPrcPrmMenuItemId(Long.valueOf((String) menuItemId));
                sysMenuPermissionService.insert(et);
            } else {
                permissions.remove(item);
            }
        }
        //删除菜单项已经有的权限（物理删除）
        if (permissions.size() > 0) {
            List<Serializable> ids = permissions.stream().map(a -> a.getId()).collect(Collectors.toList());
            sysMenuPermissionService.delete(ids.toArray(new Serializable[ids.size()]), false);
        }
    }

    /**
     * 删除菜单授权
     *
     * @param ids 主键集合
     */
    @Override
    public void permissionDel(List<Serializable> ids) {
        permissionDel(ids, true);
    }

    /**
     * 删除菜单授权
     *
     * @param ids     主键集合
     * @param isLogic 逻辑删除
     */
    @Override
    public void permissionDel(List<Serializable> ids, Boolean isLogic) {
        sysMenuPermissionService.delete(ids.toArray(new Serializable[ids.size()]), isLogic);
    }

    /**
     * 清理无效菜单授权
     */
    @Override
    public void permissionClear() {
        List<String> ids = sysMenuDao.getClearSysMenuPermissions();
        if (ids != null && ids.size() > 0) {
            sysMenuPermissionService.delete(ids.toArray(new Serializable[ids.size()]));
            sysMenuPermissionService.saveChange();
        }
    }

    /**
     * 查询菜单路径和权限集合
     *
     * @return 路径和权限码集合
     */
    @Override
    public List<MenuItemsPremissmsInfo> getMenuItemsPremissms() {
        return sysMenuDao.getMenuItemsPremissms();
    }

    /**
     * 国际化
     */
    protected String getLocalization(String content, String[] agrs) {
        return localizationService.getLocalization(content, agrs);
    }

    /**
     * 获取某个权限所有菜单权限
     *
     * @param prmId 权限外键
     * @return 菜单权限集合
     */
    @Override
    public List<MenuAllInfo> getPermissionMenus(Serializable prmId) {
        List<SysMenuItemEntity> list = sysMenuDao.getMenuItemsPremissmsByPrmId(prmId);
        List<SysMenuEntity> menus = getData(null);
        List<MenuAllInfo> result = new ArrayList<>();
        for (SysMenuEntity menu : menus) {
            MenuAllInfo et = new MenuAllInfo();
            et.setId(menu.getId().toString());
            et.setKey(menu.getId().toString());
            et.setName(menu.getMenuName());
            et.setVersion(menu.getVersion());
            et.setDescrpiton(menu.getRemark());

            List<SysMenuItemEntity> permissions = list.stream().filter(c -> Objects.equals(c.getPrcPrmMenuId(), menu.getId())).collect(Collectors.toList());
            List<MenuPremissmsInfo> menuPermission = new ArrayList<>();
            if (permissions != null) {
                for (SysMenuItemEntity pri : permissions) {
                    MenuPremissmsInfo pi = new MenuPremissmsInfo();
                    pi.setId(pri.getPrcPrmMenuPermissionId().toString());
                    pi.setKey(pri.getPrcPrmMenuPermissionId().toString());
                    pi.setTempKey(pri.getPrcPrmMenuId().toString());
                    pi.setTempId(pri.getPrcPrmMenuId().toString());
                    pi.setName(pri.getItemName());
                    pi.setIcon(pri.getIcon());
                    pi.setUrl(pri.getUrl());
                    pi.setOpenType(pri.getOpenType());
                    pi.setPosition(pri.getPosition());
                    pi.setIsHide(pri.getIsHide());
                    menuPermission.add(pi);
                }
            }
            et.setMenuPermission(menuPermission);
            result.add(et);
        }

        return result;
    }

    /**
     * 获取某个权限所有菜单权限
     *
     * @param menuId 菜单外键
     * @param prmId  权限外键
     */
    @Override
    public List<SysMenuItemPrmInfo> getPermissionMenuItems(Serializable menuId, Serializable prmId) {
        return sysMenuDao.getMenuItemsPremissmsByPrmIdMenuId(prmId, menuId);
    }

    /**
     * 获取菜单树
     *
     * @param menuName 菜单名称
     * @return 菜单节点集合
     */
    @Override
    public List<MenuNodeInfo> getMenuTree(String menuName) {
        try {
            List<SysMenuItemEntity> menuItems = getMenuItemsByMenuName(menuName);
            List<MenuNodeInfo> nodes = new ArrayList();
            //一级菜单
            List<SysMenuItemEntity> rootMenuItmes = menuItems.stream().filter(o -> o.getPosition().split("\\.").length == 1
                    && !o.getIsHide()).collect(Collectors.toList());
            for (SysMenuItemEntity menuItem : rootMenuItmes) {
                MenuNodeInfo node = getMenuNode(menuItems, menuItem);
                nodes.add(node);
            }
            //throw new Exception("sss");
            return nodes;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取菜单节点
     *
     * @param menuItems 菜单项集合
     * @param menuItem  菜单项对象
     * @return 菜单节点
     */
    private MenuNodeInfo getMenuNode(List<SysMenuItemEntity> menuItems, SysMenuItemEntity menuItem) {
        MenuNodeInfo node = new MenuNodeInfo();
        node.setId(menuItem.getId().toString());
        node.setHref(menuItem.getOpenType() == 1 ? null : menuItem.getUrl());
        node.setUrl(menuItem.getOpenType() == 1 ? menuItem.getUrl() : null);
        node.setIcon(menuItem.getIcon() == null ? "&#xe63f;" : menuItem.getIcon());
        //node.setTitle(getLocalization(menuItem.getItemName(), null));
        node.setTitle(menuItem.getItemName());
        node.setTargetType(menuItem.getOpenType() == 1 ? "_blank" : "");

        List<SysMenuItemEntity> childMenuItems = menuItems.stream().filter(o ->
                        o.getPosition().split("\\.").length == (menuItem.getPosition().split("\\.").length + 1)
                                && o.getPosition().startsWith(menuItem.getPosition() + ".") && !o.getIsHide())
                .collect(Collectors.toList());
        if (childMenuItems.size() > 0) {
            for (SysMenuItemEntity childMenuItem : childMenuItems) {
                MenuNodeInfo childNode = getMenuNode(menuItems, childMenuItem);
                if (childNode == null) {
                    node.setChildren(new ArrayList<>());
                }
                if (childNode != null) {
                    node.getChildren().add(childNode);
                }
            }
        }
        return node;
    }

    /**
     * 获取菜单项数据
     *
     * @param menuId 菜单外键
     * @return 菜单项集合
     */
    @Override
    public List<SysMenuItemEntity> getMenuItems(Serializable menuId) {
        return getMenuItems(menuId, false);
    }

    /**
     * 获取菜单项数据
     *
     * @param menuId       菜单外键
     * @param isPermission 是否授权
     * @return 菜单项集合
     */
    @Override
    public List<SysMenuItemEntity> getMenuItems(Serializable menuId, Boolean isPermission) {
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("PRC_PRM_MENU_ID", menuId.toString(), ConditionOper.Equal));
        conditions.add(new ConditionDto("IS_DELETE", "false", ConditionOper.Equal));

        List<SysMenuItemEntity> datas = sysMenuItemService.getData(conditions);
        datas.sort(Comparator.comparing(SysMenuItemEntity::getPosition, new FlatPositionComparer()));
        //只返回具有权限的菜单数据
        if (isPermission) {
            datas = getPermissionMenuItems(datas);
        }
        return datas;
    }

    /**
     * 根据菜单名称，获取菜单项
     *
     * @param menuName 菜单名称
     * @return 菜单项集合
     */
    @Override
    public List<SysMenuItemEntity> getMenuItemsByMenuName(String menuName) {
        return getMenuItemsByMenuName(menuName, true);
    }

    /**
     * 根据菜单名称，获取菜单项
     *
     * @param menuName     菜单名称
     * @param isPermission 是否授权
     * @return 菜单项集合
     */
    @Override
    public List<SysMenuItemEntity> getMenuItemsByMenuName(String menuName, Boolean isPermission) {
        List<SysMenuItemEntity> datas = new ArrayList();
        List<ConditionDto> conditions = new ArrayList();
        conditions.add(new ConditionDto("MENU_NAME", menuName, ConditionOper.Equal));
        conditions.add(new ConditionDto("IS_DELETE", "false", ConditionOper.Equal));

        SysMenuEntity menu = getData(conditions).stream().findFirst().orElse(null);
        if (menu != null) {
            datas = getMenuItems(menu.getId(), isPermission);
        }
        return datas;
    }

    private Map<String, List<String>> getMenuItemPermissionCodes(List<Serializable> ids) {
        Map<String, List<String>> dic = new HashMap<>(5);
        if (ids != null && !ids.isEmpty()) {
            List<PrmPermissionEntity> data = sysMenuDao.getMenuItemPermissionCodes(ids);
            for (PrmPermissionEntity entity : data) {
                String id = entity.getId().toString();
                String code = entity.getPermissionCode();
                dic.computeIfAbsent(id, k -> new ArrayList<>()).add(code);
            }
        }
        return dic;
    }

    /**
     * 获取具有权限的菜单项
     *
     * @param datas 菜单项集合
     * @return 菜单项集合
     */
    private List<SysMenuItemEntity> getPermissionMenuItems(List<SysMenuItemEntity> datas) {
        if (datas == null || datas.size() == 0) {
            return new ArrayList<>();
        }
        Map<String, List<String>> dic = getMenuItemPermissionCodes(datas.stream().map(o -> o.getId()).collect(Collectors.toList()));
        return getPermissionMenuItems(datas, dic);
    }

    private List<SysMenuItemEntity> getPermissionMenuItems(List<SysMenuItemEntity> datas, Map<String, List<String>> dic) {
        List<SysMenuItemEntity> items = new ArrayList<>();
        for (SysMenuItemEntity data : datas) {
            if (dic.containsKey(data.getId())) {//这里有一个问题就是用户必须是在登录状态下访问
                if (authorizationService.hasPermission(dic.get(data.getId()))) {
                    items.add(data);
                    appendParent(data.getPosition(), datas, items);
                }
            } else//没有配置权限的菜单项，默认是显示的
            {
                items.add(data);
                appendParent(data.getPosition(), datas, items);
            }
        }
        return items;
    }

    /**
     * 查找有权限的父级列表
     */
    void appendParent(String position, List<SysMenuItemEntity> datas, List<SysMenuItemEntity> items) {
        String[] positions = position.split("\\.");
        String currentPosition = "";
        for (String p : positions) {
            currentPosition += p;
            if (StringUtils.equals(currentPosition, position)) {
                currentPosition += ".";
                continue;
            }
            String finalCurrentPosition = currentPosition;
            boolean isExits = items.stream().anyMatch(w -> StringUtils.equals(w.getPosition(), finalCurrentPosition));
            if (isExits) {
                currentPosition += ".";
                continue;
            }
            String finalCurrentPosition1 = currentPosition;
            SysMenuItemEntity itemMenu = datas.stream().filter(w -> StringUtils.equals(w.getPosition(), finalCurrentPosition1)).findFirst().orElse(null);
            if (itemMenu == null) {
                currentPosition += ".";
                continue;
            }
            items.add(itemMenu);
            currentPosition += ".";
        }
    }

    /**
     * 保存菜单信息
     *
     * @param datas  菜单项集合
     * @param menuId 菜单外键
     */
    @Override
    public void backSave(List<SysMenuItemEntity> datas, Serializable menuId) {
        UpdateWrapper<SysMenuItemEntity> delUp = new UpdateWrapper<>();
        delUp.lambda().eq(SysMenuItemEntity::getPrcPrmMenuId, menuId);
        sysMenuItemService.delete(delUp, false);
        sysMenuItemService.saveChange();
        save(datas, menuId);
    }

    /**
     * 保存菜单信息
     *
     * @param datas  菜单项集合
     * @param menuId 菜单外键
     */
    @Override
    public void save(List<SysMenuItemEntity> datas, Serializable menuId) {
        List<SysMenuItemEntity> dataList = new ArrayList();

        //生成Position
        generationPosition(datas, "", dataList);

        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("PRC_PRM_MENU_ID", menuId.toString(), ConditionOper.Equal));
        conditions.add(new ConditionDto("IS_DELETE", "false", ConditionOper.Equal));

        List<SysMenuItemEntity> existDatas = sysMenuItemService.getData(conditions);
        existDatas.sort(Comparator.comparing(SysMenuItemEntity::getPosition, new FlatPositionComparer()));
        //需要新增的数据
        List<SysMenuItemEntity> addDatas = dataList.stream().filter(o -> !existDatas.stream().anyMatch(p -> Objects.equals(p.getId(), o.getId())))
                .collect(Collectors.toList());
        for (SysMenuItemEntity addData : addDatas) {
            addData.setPrcPrmMenuId(new Long(menuId.toString()));
            sysMenuItemService.insert(addData);
        }
        //需要更新的数据
        List<SysMenuItemEntity> updateDatas = dataList.stream().filter(o -> existDatas.stream().anyMatch(p -> Objects.equals(p.getId(), o.getId()))).collect(Collectors.toList());

        for (SysMenuItemEntity updateData : updateDatas) {
            SysMenuItemEntity existData = existDatas.stream().filter(o -> Objects.equals(o.getId(), updateData.getId())).findFirst().orElse(null);
            existData.setItemName(updateData.getItemName());
            existData.setOpenType(updateData.getOpenType());
            existData.setUrl(updateData.getUrl());
            existData.setPosition(updateData.getPosition());
            existData.setIsHide(updateData.getIsHide());
            existData.setIcon(updateData.getIcon());
            sysMenuItemService.update(existData);
        }
        //需要删除的数据
        List<SysMenuItemEntity> delDatas = existDatas.stream().filter(o -> !dataList.stream().anyMatch(p -> Objects.equals(o.getId(), p.getId()))).collect(Collectors.toList());
        if (delDatas.size() > 0) {
            List<Serializable> ids = delDatas.stream().map(o -> o.getId()).collect(Collectors.toList());
            sysMenuItemService.delete(ids.toArray(new Serializable[ids.size()]));
        }
    }


}