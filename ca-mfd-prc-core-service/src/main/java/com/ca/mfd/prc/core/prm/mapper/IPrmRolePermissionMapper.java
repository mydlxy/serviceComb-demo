package com.ca.mfd.prc.core.prm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.entity.PrmRolePermissionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface IPrmRolePermissionMapper extends IBaseMapper<PrmRolePermissionEntity> {
    /**
     * 获取角色无效的权限ID
     *
     * @return
     */
    List<String> getRolePermissionRemoves();
}