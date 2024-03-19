package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmTokenEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 令牌表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmTokenService extends ICrudService<PrmTokenEntity> {

    /**
     * 获取所有的数据
     *
     * @return
     */
    List<PrmTokenEntity> getAllDatas();

    /**
     * 获取令牌下面的权限
     *
     * @param tokenId 令牌外键
     * @return 权限集合
     */
    List<PrmPermissionEntity> getPermissons(String tokenId);

    /**
     * 查询所有的token权限
     *
     * @return 权限集合
     */
    List<PrmPermissionEntity> getTokenPermisions();

    /**
     * 基于token外键导出权限
     *
     * @param tokenId
     * @param fileName
     * @param response
     * @throws IOException
     */
    void exportPermesion(String tokenId, String fileName, HttpServletResponse response) throws IOException;

    /**
     * 导出所有权限 根据角色分组
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    void exportAllPermesionByToken(String fileName, HttpServletResponse response) throws IOException;

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    List<PrmTokenEntity> getListByKey(String keyword);

    /**
     * 根据appId 查询
     *
     * @param appId Appid
     * @return 返回实体
     */
    PrmTokenEntity getTokenEntityByAppId(String appId);
}