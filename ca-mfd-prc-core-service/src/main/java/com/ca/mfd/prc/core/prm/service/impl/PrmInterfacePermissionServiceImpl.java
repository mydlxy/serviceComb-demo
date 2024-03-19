package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.entity.ApiSession;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.core.prm.dto.PrmInterfacePermissionListInfo;
import com.ca.mfd.prc.core.prm.entity.PrmInterfacePermissionEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmInterfacePermissionMapper;
import com.ca.mfd.prc.core.prm.service.IPrmInterfacePermissionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 接口权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmInterfacePermissionServiceImpl extends AbstractCrudServiceImpl<IPrmInterfacePermissionMapper, PrmInterfacePermissionEntity> implements IPrmInterfacePermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PrmInterfacePermissionServiceImpl.class);
    private static final String cacheName = "PRC_PRM_INTERFACE_PERMISSION";
    private static final String cacheNameApi = "ALL_PRC_PRM_INTERFACE_PERMISSION";
    private static final Object lockObj = new Object();
    @Autowired
    private IPrmInterfacePermissionMapper prmInterfacePermissionDao;
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
        localCache.removeObject(cacheNameApi);
        localCache.removeObject("OperSession");
    }

    @Override
    public void afterDelete(Wrapper<PrmInterfacePermissionEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PrmInterfacePermissionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PrmInterfacePermissionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PrmInterfacePermissionEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PrmInterfacePermissionEntity model) {
        validData(model);
    }

    @Override
    public void beforeUpdate(PrmInterfacePermissionEntity model) {
        validData(model);
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<PrmInterfacePermissionEntity> getAllDatas() {
        List<PrmInterfacePermissionEntity> datas = localCache.getObject(cacheName);
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

    private void validData(PrmInterfacePermissionEntity model) {
        QueryWrapper<PrmInterfacePermissionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmInterfacePermissionEntity::getPrcPrmPermissionId, model.getPrcPrmPermissionId())
                .eq(PrmInterfacePermissionEntity::getPath, model.getPath())
                .ne(PrmInterfacePermissionEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("已经存在权限代码和地址" + model.getPath());
        }
    }

    /**
     * 获取数据
     *
     * @param permissionId id
     * @return a
     */
    @Override
    public List<PrmInterfacePermissionEntity> getListByPrmPermissionid(Serializable permissionId) {
        QueryWrapper<PrmInterfacePermissionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmInterfacePermissionEntity::getPrcPrmPermissionId, permissionId);
        return selectList(qry);
    }

    /**
     * 获取接口无效的权限ID
     */
    @Override
    public List<String> getInterfacePermissionRemoves() {
        return prmInterfacePermissionDao.getInterfacePermissionRemoves();
    }

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    @Override
    public List<PrmInterfacePermissionEntity> getListByKey(String keyword) {
        QueryWrapper<PrmInterfacePermissionEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<PrmInterfacePermissionEntity> lamdQry = qry.lambda()
                .orderByAsc(PrmInterfacePermissionEntity::getRemark);
        if (!StringUtils.isBlank(keyword)) {
            lamdQry.like(PrmInterfacePermissionEntity::getPath, keyword)
                    .or(c -> c.like(PrmInterfacePermissionEntity::getRemark, keyword));
        }
        return selectList(qry);
    }


    /**
     * 获取分页数据
     *
     * @param conditions
     * @param sorts
     * @param isDelete
     * @return
     */
    @Override
    public List<PrmInterfacePermissionListInfo> getInterfaceDatas(List<ConditionDto> conditions, List<SortDto> sorts, Boolean isDelete) {
        List<ConditionDto> conditionCons = MpSqlUtils.filtrationCondition(PrmInterfacePermissionEntity.class, conditions, "");
        if (sorts == null || sorts.size() == 0) {
            sorts = new ArrayList<>();
            SortDto st = new SortDto();
            st.setColumnName("LAST_UPDATE_DATE");
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        }
        List<SortDto> sortsNew = MpSqlUtils.filtrationSort(PrmInterfacePermissionEntity.class, sorts, "");
        Map<String, Object> map = new HashMap<>(10);
        map.put("wheresa", conditionCons);
        map.put("order", sortsNew);
        map.put("isDelete", isDelete == true ? 1 : 0);
        return prmInterfacePermissionDao.getInterfaceDatas(map);
    }

    /**
     * 获取分页数据
     *
     * @param conditions
     * @param sorts
     * @param pageIndex
     * @param pageSize
     * @param isDelete
     * @return
     */
    @Override
    public PageData<PrmInterfacePermissionListInfo> getInterfacePageDatas(List<ConditionDto> conditions, List<SortDto> sorts, int pageIndex, int pageSize, Boolean isDelete) {
        List<ConditionDto> conditionCons = MpSqlUtils.filtrationCondition(PrmInterfacePermissionEntity.class, conditions, "");
        if (sorts == null || sorts.size() == 0) {
            sorts = new ArrayList<>();
            SortDto st = new SortDto();
            st.setColumnName("LAST_UPDATE_DATE");
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        }
        List<SortDto> sortsNew = MpSqlUtils.filtrationSort(PrmInterfacePermissionEntity.class, sorts, "");
        Map<String, Object> map = new HashMap<>(10);
        map.put("wheresa", conditionCons);
        map.put("order", sortsNew);
        map.put("isDelete", isDelete == true ? 1 : 0);

        PageData<PrmInterfacePermissionListInfo> page = new PageData<>();
        page.setPageIndex(pageIndex <= 0 ? 1 : pageIndex);
        page.setPageSize(pageSize);
        Page<PrmInterfacePermissionListInfo> mpage = new Page<>(pageIndex, pageSize);
        Page<PrmInterfacePermissionListInfo> pdata = prmInterfacePermissionDao.getInterfacePageDatas(mpage, map);
        page.setTotal((int) pdata.getTotal());
        page.setDatas(pdata.getRecords());
        return page;
    }

    @Override
    public List<ApiSession> getApiSession() {
        try {
            Function<Object, List<ApiSession>> getDataFunc = (obj) -> {
                List<ApiSession> lst = prmInterfacePermissionDao.getApiSessionData();
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<ApiSession> caches = localCache.getObject(cacheName, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ArrayList<>();
    }

}