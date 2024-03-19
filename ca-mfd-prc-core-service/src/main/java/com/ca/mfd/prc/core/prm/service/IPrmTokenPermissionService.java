package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.dto.TokenPermissionDTO;
import com.ca.mfd.prc.core.prm.dto.UserRoleMsg;
import com.ca.mfd.prc.core.prm.entity.PrmTokenPermissionEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 令牌权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmTokenPermissionService extends ICrudService<PrmTokenPermissionEntity> {

    /**
     * 获取权限
     *
     * @param permissionId 权限ID
     * @return 集合
     */
    List<TokenPermissionDTO> getTokenPermissions(Serializable permissionId);

    /**
     * 获取token无效的权限ID
     *
     * @return
     */
    List<String> getTokenPermissionRemoves();

    /**
     * 获取所有的数据
     *
     * @return 令牌权限数据
     */
    List<PrmTokenPermissionEntity> getAllDatas();

    /**
     * 基于权限码保存数据
     *
     * @param prmId 权限外键
     * @param datas 令牌外键集合
     * @return 集合
     */
    void permissionSave(Serializable prmId, List<String> datas);

    /**
     * 根据 dataId 获取令牌权限CODE
     *
     * @param dataId 参数ID
     * @return 返回CODE
     */
    List<UserRoleMsg> getPermissionQuery(Serializable dataId);

    /**
     * 根据 dataId 获取路径
     *
     * @param dataId dataId 参数ID
     * @return 返回路径
     */
    List<UserRoleMsg> getPathQuery(Serializable dataId);
}