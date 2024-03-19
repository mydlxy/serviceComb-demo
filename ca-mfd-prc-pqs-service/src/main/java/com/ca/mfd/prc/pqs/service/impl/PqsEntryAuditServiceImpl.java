package com.ca.mfd.prc.pqs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.dto.PqsEntryPageFilter;
import com.ca.mfd.prc.pqs.entity.PqsEntryAuditEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryAuditMapper;
import com.ca.mfd.prc.pqs.remote.app.core.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pqs.service.IPqsEntryAuditService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 质检工单-评审工单服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsEntryAuditServiceImpl extends AbstractCrudServiceImpl<IPqsEntryAuditMapper, PqsEntryAuditEntity> implements IPqsEntryAuditService {

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_AUDIT";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryAuditEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryAuditEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryAuditEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryAuditEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取质检工单-评审工单数据
     *
     * @return
     */
    @Override
    public List<PqsEntryAuditEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryAuditEntity>> getDataFunc = (obj) -> {
            List<PqsEntryAuditEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryAuditEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public PageData getEntryList(PqsEntryPageFilter filter) {
        int[] statusArray = filter.getStaus();
        QueryWrapper<PqsEntryAuditEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsEntryAuditEntity> lambda = queryWrapper.lambda();
        lambda.eq(PqsEntryAuditEntity::getEntryType, filter.getEntryType()).in(PqsEntryAuditEntity::getStatus, Arrays.asList(Arrays.stream(statusArray).boxed().toArray(Integer[]::new)));

        if (!Strings.isNullOrEmpty(filter.getKey())) {
            lambda.nested(n -> n.like(PqsEntryAuditEntity::getMaterialNo, filter.getKey())
                    .or(o -> o.like(PqsEntryAuditEntity::getMaterialName, filter.getKey()))
                    .or(o -> o.like(PqsEntryAuditEntity::getMaterialNo, filter.getKey()))
            );
        }
        /*if (!Strings.isNullOrEmpty(filter.getAreaCode())) {
            lambda.eq(PqsEntryAuditEntity::getAreaCode, filter.getAreaCode());
        }*/
        lambda.orderByAsc(PqsEntryAuditEntity::getStatus);
        Page<PqsEntryAuditEntity> page = (Page<PqsEntryAuditEntity>) getDataByPage(queryWrapper, filter.getPageIndex(), filter.getPageSize());


        List<PqsEntryAuditEntity> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            List<SysConfigurationEntity> sysConfig = sysConfigurationProvider.getAllDatas();
            for (PqsEntryAuditEntity entity : page.getRecords()) {
                SysConfigurationEntity sysConfigurationEntity = sysConfig.stream().filter(c -> "PqsEntryStatus_Normal".equals(c.getCategory()) && c.getValue().equals(entity.getStatus().toString())).findFirst().orElse(null);
                entity.setStatusDescription(sysConfigurationEntity != null ? sysConfigurationEntity.getText() : "");
                list.add(entity);
            }
        }
        PageData<PqsEntryAuditEntity> result = new PageData<>();
        result.setDatas(list);
        result.setTotal((int) page.getTotal());
        result.setPageIndex(filter.getPageIndex());
        result.setPageSize(filter.getPageSize());

        return result;
    }
}