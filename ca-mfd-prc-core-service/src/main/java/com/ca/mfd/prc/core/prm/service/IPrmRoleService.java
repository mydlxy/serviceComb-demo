package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 角色表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmRoleService extends ICrudService<PrmRoleEntity> {


    /**
     * 获取绑定了该角色的用户
     *
     * @param roleId 角色外键
     * @return 绑定了该角色的用户集合
     */
    List<PrmUserEntity> getUsers(Serializable roleId);

    /**
     * 基于角色保存权限数据
     *
     * @param role  角色
     * @param datas 权限
     */
    void roleSave(PrmRoleEntity role, List<Serializable> datas);

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    List<PrmRoleEntity> getListByKey(String keyword);


    /**
     * 导出绑定了该角色的用户
     *
     * @param roleId
     * @param fileName
     * @param response
     * @throws IOException
     */
    void exportPermesion(Serializable roleId, String fileName, HttpServletResponse response) throws IOException;

    /**
     * 导出绑定了该角色的用户
     *
     * @param roleId
     * @param fileName
     * @param response
     * @throws IOException
     */
    void exportUsers(Serializable roleId, String fileName, HttpServletResponse response) throws IOException;

    /**
     * 导出所有权限 根据角色分组
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    void exportAllPermesionByRole(String fileName, HttpServletResponse response) throws IOException;

    /**
     * 导出所有角色下的所有用户
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    void exportAllUsersByRole(String fileName, HttpServletResponse response) throws IOException;

    /**
     * 获取角色权限
     *
     * @param roleId
     * @return 权限集合
     */
    List<PrmPermissionEntity> getPermissons(Serializable roleId);

    /**
     * 查询所有权限
     *
     * @return 权限集合
     */
    List<PrmPermissionEntity> getRolePermisions();
}