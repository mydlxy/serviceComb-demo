package com.ca.mfd.prc.core.prm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.dto.PrmUserPermissionView;
import com.ca.mfd.prc.core.prm.entity.PrmUserPermissionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * 用户权限关联表
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface IPrmUserPermissionMapper extends IBaseMapper<PrmUserPermissionEntity> {

    /**
     * 获取用户无效的权限ID
     */
    List<String> getUserPermissionRemoves();

    /**
     * 获取用户权限集合
     *
     * @param userId 用户ID
     */
    List<PrmUserPermissionView> getUserPermissions(Serializable userId);
}