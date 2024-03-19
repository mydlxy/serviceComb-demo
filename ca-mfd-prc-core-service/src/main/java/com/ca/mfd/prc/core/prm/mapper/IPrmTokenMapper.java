package com.ca.mfd.prc.core.prm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmTokenEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * 令牌表
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface IPrmTokenMapper extends IBaseMapper<PrmTokenEntity> {

    /**
     * 获取角色权限
     *
     * @param tokenId
     * @return
     */
    List<PrmPermissionEntity> getPermissions(Serializable tokenId);

    /**
     * 获取角色权限
     *
     * @return
     */
    List<PrmPermissionEntity> getTokenPermisions();
}