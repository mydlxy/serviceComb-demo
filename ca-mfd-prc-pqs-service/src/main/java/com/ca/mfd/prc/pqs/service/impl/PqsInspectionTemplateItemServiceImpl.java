package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateItemEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsInspectionTemplateItemMapper;
import com.ca.mfd.prc.pqs.service.IPqsInspectionTemplateItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 检验模板-项目服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsInspectionTemplateItemServiceImpl extends AbstractCrudServiceImpl<IPqsInspectionTemplateItemMapper, PqsInspectionTemplateItemEntity> implements IPqsInspectionTemplateItemService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_INSPECTION_TEMPLATE_ITEM";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsInspectionTemplateItemEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsInspectionTemplateItemEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsInspectionTemplateItemEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsInspectionTemplateItemEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 从缓存中获取检验模板信息
     *
     * @return 检验模板列表
     */
    @Override
    public List<PqsInspectionTemplateItemEntity> getAllDatas() {
        Function<Object, ? extends List<PqsInspectionTemplateItemEntity>> getDataFunc = (obj) -> {
            List<PqsInspectionTemplateItemEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsInspectionTemplateItemEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public void saveExcelData(List<PqsInspectionTemplateItemEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsInspectionTemplateItemEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsInspectionTemplateItemEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getPrcPqsInspectionTemplateId().equals(a.getPrcPqsInspectionTemplateId())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsInspectionTemplateItemEntity> insertList = entities.stream()
                .filter(e -> !updateList.contains(e)).collect(Collectors.toList());

        if (insertList.size() > 0) {
            insertBatch(insertList, insertList.size());
        }
        if (updateList.size() > 0) {
            updateBatchById(updateList, updateList.size());
        }
        removeCache();
        saveChange();
    }

    @Override
    public void beforeInsert(PqsInspectionTemplateItemEntity entity) {
        if (entity.isDataCheck()) {
            QueryWrapper<PqsInspectionTemplateItemEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsInspectionTemplateItemEntity::getItemCode, entity.getItemCode()).eq(PqsInspectionTemplateItemEntity::getPrcPqsInspectionTemplateId, entity.getPrcPqsInspectionTemplateId());
            PqsInspectionTemplateItemEntity anomalyEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("编码" + anomalyEntity.getItemCode() + "已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(PqsInspectionTemplateItemEntity entity) {
        if (entity.isDataCheck()) {
            QueryWrapper<PqsInspectionTemplateItemEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsInspectionTemplateItemEntity::getItemCode, entity.getItemCode()).eq(PqsInspectionTemplateItemEntity::getPrcPqsInspectionTemplateId, entity.getPrcPqsInspectionTemplateId()).ne(PqsInspectionTemplateItemEntity::getId, entity.getId());
            PqsInspectionTemplateItemEntity anomalyEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("编码" + anomalyEntity.getItemCode() + "," + "已存在,请不要重复添加");
            }
        }
        removeCache();
    }
}