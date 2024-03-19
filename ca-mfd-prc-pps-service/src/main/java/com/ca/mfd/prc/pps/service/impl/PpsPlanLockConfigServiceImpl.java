package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsPlanLockConfigEntity;
import com.ca.mfd.prc.pps.mapper.IPpsPlanLockConfigMapper;
import com.ca.mfd.prc.pps.service.IPpsPlanLockConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author eric.zhou
 * @Description: 生产计划锁定配置服务实现
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Service
public class PpsPlanLockConfigServiceImpl extends AbstractCrudServiceImpl<IPpsPlanLockConfigMapper, PpsPlanLockConfigEntity> implements IPpsPlanLockConfigService {

    private final String cacheName = "PRC_PPS_PLAN_LOCK_CONFIG";
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PpsPlanLockConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PpsPlanLockConfigEntity model) {
        QueryWrapper<PpsPlanLockConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanLockConfigEntity::getOrderCategory, model.getOrderCategory());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("此订单大类已设置，不能重复设置");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsPlanLockConfigEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void afterUpdate(PpsPlanLockConfigEntity model) {
        QueryWrapper<PpsPlanLockConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanLockConfigEntity::getOrderCategory, model.getOrderCategory())
                .ne(PpsPlanLockConfigEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("此订单大类已设置，不能重复设置");
        }
        removeCache();
    }

    /**
     * 获取所有的数据
     *
     * @return List<PpsPlanLockConfigEntity>
     */
    @Override
    public List<PpsPlanLockConfigEntity> getAllDatas() {
        Function<Object, ? extends List<PpsPlanLockConfigEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, -1);
    }

}