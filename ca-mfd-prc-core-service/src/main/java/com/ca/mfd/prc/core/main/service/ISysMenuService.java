package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.dto.core.MenuItemsPremissmsInfo;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.dto.MenuNodeInfo;
import com.ca.mfd.prc.core.main.dto.MenuAllInfo;
import com.ca.mfd.prc.core.main.dto.SysMenuItemPrmInfo;
import com.ca.mfd.prc.core.main.entity.SysMenuEntity;
import com.ca.mfd.prc.core.main.entity.SysMenuItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ISysMenuService extends ICrudService<SysMenuEntity> {

    /**
     * 获取菜单
     *
     * @param menuId  菜单项ID
     * @param version 版本
     * @return 菜单项
     */
    SysMenuEntity getMenuByFirst(Serializable menuId, Integer version);

    /**
     * 查询菜单路径和权限集合
     *
     * @return 路径和权限码集合
     */
    List<MenuItemsPremissmsInfo> getMenuItemsPremissms();

    /**
     * 获取菜单项数据
     *
     * @param menuId 菜单外键
     * @return 菜单项集合
     */
    List<SysMenuItemEntity> getMenuItems(Serializable menuId);

    /**
     * 获取菜单项数据
     *
     * @param menuId       菜单外键
     * @param isPermission 是否授权
     * @return 菜单项集合
     */
    List<SysMenuItemEntity> getMenuItems(Serializable menuId, Boolean isPermission);

    /**
     * 根据菜单名称，获取菜单项
     *
     * @param menuName 菜单名称
     * @return 菜单项集合
     */
    List<SysMenuItemEntity> getMenuItemsByMenuName(String menuName);

    /**
     * 根据菜单名称，获取菜单项
     *
     * @param menuName     菜单名称
     * @param isPermission 是否授权
     * @return 菜单项集合
     */
    List<SysMenuItemEntity> getMenuItemsByMenuName(String menuName, Boolean isPermission);

    /**
     * 保存菜单信息
     *
     * @param datas  菜单项集合
     * @param menuId 菜单外键
     */
    void backSave(List<SysMenuItemEntity> datas, Serializable menuId);

    /**
     * 保存菜单信息
     *
     * @param datas  菜单项集合
     * @param menuId 菜单外键
     */
    void save(List<SysMenuItemEntity> datas, Serializable menuId);

    /**
     * 获取菜单关联的权限
     *
     * @param menuItemId 菜单项外键
     * @return 权限外键集合
     */
    List<String> getMenuItemPermissions(Serializable menuItemId);

    /**
     * 保存菜单的权限
     *
     * @param menuItemId    菜单项外键
     * @param permissionIds 权限集合
     */
    void savePermissions(Serializable menuItemId, List<Serializable> permissionIds);

    /**
     * 保存菜单的权限
     *
     * @param prmId       权限外键
     * @param menuId      菜单外键
     * @param menuItemIds 菜单项集合
     */
    void permissionSave(Serializable prmId, Serializable menuId, List<Serializable> menuItemIds);

    /**
     * 获取菜单树
     *
     * @param menuName 菜单名称
     * @return 菜单节点集合
     */
    List<MenuNodeInfo> getMenuTree(String menuName);

    /**
     * 获取某个权限所有菜单权限
     *
     * @param prmId 权限外键
     * @return 菜单权限集合
     */
    List<MenuAllInfo> getPermissionMenus(Serializable prmId);

    /**
     * 获取某个权限所有菜单权限
     *
     * @param menuId 菜单外键
     * @param prmId  权限外键
     */
    List<SysMenuItemPrmInfo> getPermissionMenuItems(Serializable menuId, Serializable prmId);

    /**
     * 删除菜单授权
     *
     * @param ids     主键集合
     * @param isLogic 逻辑删除
     */
    void permissionDel(List<Serializable> ids, Boolean isLogic);

    /**
     * 删除菜单授权
     *
     * @param ids 主键集合
     */
    void permissionDel(List<Serializable> ids);

    /**
     * 清理无效菜单授权
     */
    void permissionClear();

}