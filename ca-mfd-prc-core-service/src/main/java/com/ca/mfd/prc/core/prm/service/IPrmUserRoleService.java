package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.dto.UserRoleMsg;
import com.ca.mfd.prc.core.prm.entity.PrmUserRoleEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 用户角色表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmUserRoleService extends ICrudService<PrmUserRoleEntity> {
    /**
     * 获取用户角色集合
     *
     * @param userId 用户ID
     */
    List<UserRoleMsg> getRoleMsgs(Serializable userId);

    /**
     * 获取用户权限集合
     *
     * @param userId 用户ID
     */
    List<UserRoleMsg> getRolePermissions(Serializable userId);
}