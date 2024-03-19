package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.entity.PrmRolePermissionEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 角色权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmRolePermissionService extends ICrudService<PrmRolePermissionEntity> {

    /**
     * 查询列表
     *
     * @param roleId 角色ID
     * @return 集合
     */
    List<PrmRolePermissionEntity> getByPrmRoleId(Serializable roleId);

    /***
     * 获取角色无效的权限ID
     * @return
     */
    List<String> getRolePermissionRemoves();
}