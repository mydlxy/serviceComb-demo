package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.main.entity.SysMenuItemVersionEntity;
import com.ca.mfd.prc.core.main.mapper.ISysMenuItemVersionMapper;
import com.ca.mfd.prc.core.main.service.ISysMenuItemVersionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单版本管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysMenuItemVersionServiceImpl extends AbstractCrudServiceImpl<ISysMenuItemVersionMapper, SysMenuItemVersionEntity> implements ISysMenuItemVersionService {

    @Override
    public List<SysMenuItemVersionEntity> getMenuVersion(String menuId) {
        QueryWrapper<SysMenuItemVersionEntity> qry = new QueryWrapper<>();
        qry.lambda().
                eq(SysMenuItemVersionEntity::getPrcPrmMenuId, menuId).orderByDesc(SysMenuItemVersionEntity::getCreationDate)
                .select(SysMenuItemVersionEntity::getId, SysMenuItemVersionEntity::getMenuName, SysMenuItemVersionEntity::getVersion, SysMenuItemVersionEntity::getCreatedBy,
                        SysMenuItemVersionEntity::getCreatedUser, SysMenuItemVersionEntity::getCreationDate, SysMenuItemVersionEntity::getPrcPrmMenuId,
                        SysMenuItemVersionEntity::getIsDelete, SysMenuItemVersionEntity::getLastUpdateDate, SysMenuItemVersionEntity::getLastUpdatedBy, SysMenuItemVersionEntity::getLastUpdatedUser);
        return selectList(qry);
    }
}