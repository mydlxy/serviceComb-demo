package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.main.entity.SysMenuItemVersionEntity;

import java.util.List;

/**
 * 菜单版本管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ISysMenuItemVersionService extends ICrudService<SysMenuItemVersionEntity> {

    /**
     * 获取菜单版本
     */
    List<SysMenuItemVersionEntity> getMenuVersion(String menuId);
}