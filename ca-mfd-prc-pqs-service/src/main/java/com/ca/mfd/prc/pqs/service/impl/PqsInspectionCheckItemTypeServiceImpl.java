package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsInspectionCheckItemTypeEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsInspectionCheckItemTypeMapper;
import com.ca.mfd.prc.pqs.service.IPqsInspectionCheckItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 检验类型技术配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsInspectionCheckItemTypeServiceImpl extends AbstractCrudServiceImpl<IPqsInspectionCheckItemTypeMapper, PqsInspectionCheckItemTypeEntity> implements IPqsInspectionCheckItemTypeService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_INSPECTION_CHECK_ITEM_TYPE";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsInspectionCheckItemTypeEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsInspectionCheckItemTypeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsInspectionCheckItemTypeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsInspectionCheckItemTypeEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 从缓存中获取检验模板信息
     *
     * @return 检验模板列表
     */
    @Override
    public List<PqsInspectionCheckItemTypeEntity> getAllDatas() {
        Function<Object, ? extends List<PqsInspectionCheckItemTypeEntity>> getDataFunc = (obj) -> {
            List<PqsInspectionCheckItemTypeEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsInspectionCheckItemTypeEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public List<ComboDataDTO> getCombo() {
        List<ComboDataDTO> collect = this.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsInspectionCheckItemTypeEntity::getDisplayNo)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setText(c.getTypeName());
                    dto.setValue(c.getTypeCode());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }

}