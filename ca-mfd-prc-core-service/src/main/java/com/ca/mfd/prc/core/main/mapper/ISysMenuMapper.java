package com.ca.mfd.prc.core.main.mapper;

import com.ca.mfd.prc.common.dto.core.MenuItemsPremissmsInfo;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.main.dto.SysMenuItemPrmInfo;
import com.ca.mfd.prc.core.main.entity.SysMenuEntity;
import com.ca.mfd.prc.core.main.entity.SysMenuItemEntity;
import com.ca.mfd.prc.core.main.entity.SysMenuPermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface ISysMenuMapper extends IBaseMapper<SysMenuEntity> {

    /**
     * 获取权限
     */
    List<SysMenuPermissionEntity> getSysMenuPermissions(Serializable prmId, Serializable menuId);

    /**
     * 获取无效权限
     */
    List<String> getClearSysMenuPermissions();

    /**
     * 获取菜单权限
     */
    List<MenuItemsPremissmsInfo> getMenuItemsPremissms();

    /**
     * 获取权限
     */
    List<SysMenuItemEntity> getMenuItemsPremissmsByPrmId(Serializable prmId);

    /**
     * 获取权限
     */
    List<SysMenuItemPrmInfo> getMenuItemsPremissmsByPrmIdMenuId(Serializable prmId, Serializable menuId);

    /**
     * 获取权限
     */
    List<PrmPermissionEntity> getMenuItemPermissionCodes(@Param("itemIds") List<Serializable> itemIds);

}