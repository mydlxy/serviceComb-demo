package com.ca.mfd.prc.core.prm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * 角色表
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface IPrmRoleMapper extends IBaseMapper<PrmRoleEntity> {
    /***
     * 获取角色权限
     * @param roleId
     * @return
     */
    List<PrmPermissionEntity> getPermissions(Serializable roleId);

    /***
     * 获取角色权限
     * @return
     */
    List<PrmPermissionEntity> getRolePermisions();

}