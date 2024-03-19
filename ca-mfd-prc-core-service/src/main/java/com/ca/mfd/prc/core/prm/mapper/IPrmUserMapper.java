package com.ca.mfd.prc.core.prm.mapper;

import com.ca.mfd.prc.common.entity.UserData;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户表
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface IPrmUserMapper extends IBaseMapper<PrmUserEntity> {

    /**
     * 获取用户权限
     *
     * @param userId 用户ID
     * @return
     */
    List<PrmPermissionEntity> getUserTemporaryPermissions(Serializable userId);

    /**
     * 获取已经授权的用户集合
     *
     * @return
     */
    List<PrmUserEntity> getPrmUserInfos();

    /**
     * 获取用户权限集合
     *
     * @param userId 用户ID
     * @return
     */
    List<PrmPermissionEntity> prmPermissionInfos(Serializable userId);

    /**
     * 获取已经授权的用户外键集合
     *
     * @return
     */
    List<String> getPrmPermissionUserIdInfos();

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    List<PrmRoleEntity> getUserRoles(Serializable userId);

    /**
     * 获取用户所有权限集合
     *
     * @param userId
     * @param recycleDt
     * @return
     */
    List<String> getPermissions(Serializable userId, Date recycleDt);

    /**
     * 获取用户
     *
     * @param roleId
     * @return
     */
    List<PrmUserEntity> getUsers(Serializable roleId);

    /**
     * 获取校色下全部用户
     *
     * @return
     */
    List<PrmUserEntity> getRoleUsers();

    /**
     * 根据用户id获取用户信息
     *
     * @param ids
     * @return
     */
    List<UserData> getUsersByRoleId(List<Serializable> ids);

    /**
     * 查看设置当前角色用户数据
     *
     * @return
     */
    List<PrmUserEntity> userTempPermissionQuery();
}