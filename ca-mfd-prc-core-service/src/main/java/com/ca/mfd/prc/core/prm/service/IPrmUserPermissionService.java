package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.dto.PrmUserPermissionView;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserPermissionEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户权限关联表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmUserPermissionService extends ICrudService<PrmUserPermissionEntity> {

    /**
     * 获取用户权限集合
     *
     * @param userId 用户ID
     */
    List<PrmUserPermissionView> getUserPermissions(Serializable userId);

    /**
     * 获取用户无效的权限ID
     */
    List<String> getUserPermissionRemoves();

    /**
     * 获取用户权限
     *
     * @param userId       用户外键
     * @param permissionId 权限ID
     * @return 权限集合
     */
    PrmUserPermissionEntity getUserFirst(Serializable userId, Serializable permissionId);

    /**
     * 获取用户权限
     *
     * @param userId 用户外键
     * @return 权限集合
     */
    List<PrmPermissionEntity> getUserTemporaryPermissions(Serializable userId);

    /**
     * 保存用户临时权限数据
     *
     * @param datas  临时权限集合
     * @param userId 用户外键
     */
    void save(List<PrmUserPermissionEntity> datas, Serializable userId);

    /**
     * 导出所有临时权限
     */
    void exportDatas(String fileName, HttpServletResponse response) throws IOException;

    /**
     * ua使用 获取已经授权的用户外键集合
     *
     * @return 用户外键集合
     */
    List<String> getPrmPermissionUserIdInfos();

    /**
     * 获取已经授权的用户集合
     *
     * @return 用户集合
     */
    List<PrmUserEntity> getPrmUserInfos();

    /**
     * 获取用户权限集合
     *
     * @param userId 用户外键
     * @return 权限集合
     */
    List<PrmPermissionEntity> getPrmPermissionEntitys(Serializable userId);

    /**
     * 保存用户临时权限数据-新
     *
     * @param userId 权限外键
     * @param datas  临时权限集合
     */
    void userSave(Serializable userId, Map<String, Date> datas);
}