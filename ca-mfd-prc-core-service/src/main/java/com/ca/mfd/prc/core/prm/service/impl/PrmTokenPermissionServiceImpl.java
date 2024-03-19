package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.dto.TokenPermissionDTO;
import com.ca.mfd.prc.core.prm.dto.UserRoleMsg;
import com.ca.mfd.prc.core.prm.entity.PrmTokenPermissionEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmTokenPermissionMapper;
import com.ca.mfd.prc.core.prm.service.IPrmTokenPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 令牌权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmTokenPermissionServiceImpl extends AbstractCrudServiceImpl<IPrmTokenPermissionMapper, PrmTokenPermissionEntity> implements IPrmTokenPermissionService {

    private static final String cacheName = "PRC_PRM_TOKEN_PERMISSION";
    private static final Object lockObj = new Object();
    @Autowired
    private IPrmTokenPermissionMapper prmTokenPermissionDao;
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PrmTokenPermissionEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PrmTokenPermissionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PrmTokenPermissionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PrmTokenPermissionEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<PrmTokenPermissionEntity> getAllDatas() {
        List<PrmTokenPermissionEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

    /**
     * 获取权限
     *
     * @param permissionId 权限ID
     * @return 集合
     */
    @Override
    public List<TokenPermissionDTO> getTokenPermissions(Serializable permissionId) {
        List<TokenPermissionDTO> list = prmTokenPermissionDao.getTokenPermissions(permissionId);
        if (list != null) {
            for (TokenPermissionDTO et : list) {
                et.setKey(et.getId().toLowerCase());
                et.setTempKey(et.getTempId().toLowerCase());
            }
        }
        return list;
    }

    /**
     * 获取token无效的权限ID
     */
    @Override
    public List<String> getTokenPermissionRemoves() {
        return prmTokenPermissionDao.getTokenPermissionRemoves();
    }

    /**
     * 基于权限码保存数据
     *
     * @param prmId 权限外键
     * @param datas 令牌外键集合
     * @return 集合
     */
    @Override
    public void permissionSave(Serializable prmId, List<String> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas = datas.stream().distinct().collect(Collectors.toList());
        QueryWrapper<PrmTokenPermissionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmTokenPermissionEntity::getPrcPrmPermissionId, prmId);
        List<PrmTokenPermissionEntity> existDatas = selectList(qry);

        for (String item : datas) {
            if (UUIDUtils.isGuidEmpty(item)) {
                continue;
            }
            PrmTokenPermissionEntity aa = existDatas.stream().filter(w ->
                    StringUtils.equals(w.getPrcPrmTokenId().toString(), item)).findFirst().orElse(null);
            if (aa == null) {
                PrmTokenPermissionEntity et = new PrmTokenPermissionEntity();
                et.setPrcPrmPermissionId(Long.valueOf(prmId.toString()));
                et.setPrcPrmTokenId(Long.valueOf(item));
                insert(et);
            } else {
                existDatas.remove(aa);
            }
        }
        if (existDatas.size() > 0) {
            List<Serializable> delIds = existDatas.stream().map(o -> o.getId())
                    .collect(Collectors.toList());
            this.delete(delIds.toArray(new Serializable[0]));
        }
    }

    /**
     * 根据 dataId 获取令牌权限CODE
     *
     * @param dataId 参数ID
     * @return 返回CODE
     */
    @Override
    public List<UserRoleMsg> getPermissionQuery(Serializable dataId) {
        return prmTokenPermissionDao.getPermissionQuery(dataId);
    }

    /**
     * 根据 dataId 获取路径
     *
     * @param dataId dataId 参数ID
     * @return 返回路径
     */
    @Override
    public List<UserRoleMsg> getPathQuery(Serializable dataId) {
        return prmTokenPermissionDao.getPathQuery(dataId);
    }
}