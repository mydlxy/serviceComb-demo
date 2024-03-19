package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;

import java.util.List;

/**
 * 权限
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmPermissionService extends ICrudService<PrmPermissionEntity> {
    /***
     * 获取所有的数据
     * @return
     */
    List<PrmPermissionEntity> getAllDatas();

    /**
     * 获取首条数据
     *
     * @param id
     * @return 数据
     */
    PrmPermissionEntity getFirstById(Long id);

    /**
     * 从缓存中获取，该用户拥有的权限数据
     *
     * @return 用户拥有的权限数据
     */
    List<PrmPermissionEntity> getAllOwnDatas();

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    List<PrmPermissionEntity> getListByKey(String keyword);
}