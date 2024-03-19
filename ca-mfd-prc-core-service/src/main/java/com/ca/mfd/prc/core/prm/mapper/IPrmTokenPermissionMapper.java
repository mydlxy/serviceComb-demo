package com.ca.mfd.prc.core.prm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.dto.TokenPermissionDTO;
import com.ca.mfd.prc.core.prm.dto.UserRoleMsg;
import com.ca.mfd.prc.core.prm.entity.PrmTokenPermissionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * 令牌权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface IPrmTokenPermissionMapper extends IBaseMapper<PrmTokenPermissionEntity> {

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
     * 根据 dataId 获取令牌权限CODE
     *
     * @param dataId
     * @return
     */
    List<UserRoleMsg> getPermissionQuery(Serializable dataId);

    /**
     * 根据 dataId 获取路径
     *
     * @param dataId
     * @return
     */
    List<UserRoleMsg> getPathQuery(Serializable dataId);
}