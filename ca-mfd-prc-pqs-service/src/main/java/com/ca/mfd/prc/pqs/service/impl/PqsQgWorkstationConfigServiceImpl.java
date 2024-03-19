package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQgWorkstationConfigEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQgWorkstationConfigMapper;
import com.ca.mfd.prc.pqs.service.IPqsQgWorkstationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 质量门功能配置服务实现
 * @date 2023年09月06日
 * @变更说明 BY inkelink At 2023年09月06日
 */
@Service
public class PqsQgWorkstationConfigServiceImpl extends AbstractCrudServiceImpl<IPqsQgWorkstationConfigMapper, PqsQgWorkstationConfigEntity> implements IPqsQgWorkstationConfigService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QG_WORKSTATION_CONFIG";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQgWorkstationConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQgWorkstationConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQgWorkstationConfigEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQgWorkstationConfigEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsQgWorkstationConfigEntity entity) {
        QueryWrapper<PqsQgWorkstationConfigEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsQgWorkstationConfigEntity::getWorkstationCode, entity.getWorkstationCode());
        PqsQgWorkstationConfigEntity qgWorkstationConfigEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
        if (qgWorkstationConfigEntity != null) {
            throw new InkelinkException("工位代码已存在,请不要重复添加");
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(PqsQgWorkstationConfigEntity entity) {
        QueryWrapper<PqsQgWorkstationConfigEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsQgWorkstationConfigEntity::getWorkstationCode, entity.getWorkstationCode());
        queryWrapper.lambda().ne(PqsQgWorkstationConfigEntity::getId, entity.getId());
        PqsQgWorkstationConfigEntity qgWorkstationConfigEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
        if (qgWorkstationConfigEntity != null) {
            throw new InkelinkException("工位代码已存在,请不要重复添加");
        }
        removeCache();
    }

    @Override
    public List<PqsQgWorkstationConfigEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQgWorkstationConfigEntity>> getDataFunc = (obj) -> {
            List<PqsQgWorkstationConfigEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQgWorkstationConfigEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }
}