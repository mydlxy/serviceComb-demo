package com.ca.mfd.prc.core.prm.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.prm.dto.UserRoleMsg;
import com.ca.mfd.prc.core.prm.entity.PrmUserRoleEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmUserRoleMapper;
import com.ca.mfd.prc.core.prm.service.IPrmUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 用户角色表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmUserRoleServiceImpl extends AbstractCrudServiceImpl<IPrmUserRoleMapper, PrmUserRoleEntity> implements IPrmUserRoleService {


    @Autowired
    private IPrmUserRoleMapper prmUserRoleMapper;

    /**
     * 获取用户角色集合
     *
     * @param userId 用户ID
     */
    @Override
    public List<UserRoleMsg> getRoleMsgs(Serializable userId) {
        return prmUserRoleMapper.getRoleMsgs(userId);
    }

    /**
     * 获取用户权限集合
     *
     * @param userId 用户ID
     */
    @Override
    public List<UserRoleMsg> getRolePermissions(Serializable userId) {
        return prmUserRoleMapper.getRolePermissions(userId);
    }
}