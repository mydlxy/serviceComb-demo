package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.prm.entity.PrmRolePermissionEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmRolePermissionMapper;
import com.ca.mfd.prc.core.prm.service.IPrmRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 角色权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmRolePermissionServiceImpl extends AbstractCrudServiceImpl<IPrmRolePermissionMapper, PrmRolePermissionEntity> implements IPrmRolePermissionService {

    @Autowired
    private IPrmRolePermissionMapper prmRolePermissionDao;

    /**
     * 获取角色无效的权限ID
     */
    @Override
    public List<String> getRolePermissionRemoves() {
        return prmRolePermissionDao.getRolePermissionRemoves();
    }

    /**
     * 查询列表
     *
     * @param roleId 角色ID
     * @return 集合
     */
    @Override
    public List<PrmRolePermissionEntity> getByPrmRoleId(Serializable roleId) {

        QueryWrapper<PrmRolePermissionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmRolePermissionEntity::getPrcPrmRoleId, roleId);
        return selectList(qry);
    }

}