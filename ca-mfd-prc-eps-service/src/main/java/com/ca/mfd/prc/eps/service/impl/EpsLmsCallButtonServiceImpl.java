package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.entity.EpsLmsCallButtonEntity;
import com.ca.mfd.prc.eps.mapper.IEpsLmsCallButtonMapper;
import com.ca.mfd.prc.eps.service.IEpsLmsCallButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 物流拉动按钮配置服务实现
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@Service
public class EpsLmsCallButtonServiceImpl extends AbstractCrudServiceImpl<IEpsLmsCallButtonMapper, EpsLmsCallButtonEntity> implements IEpsLmsCallButtonService {

    private final String cacheName = "PRC_EPS_LMS_CALL_BUTTON";

    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<EpsLmsCallButtonEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsLmsCallButtonEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(EpsLmsCallButtonEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsLmsCallButtonEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(EpsLmsCallButtonEntity model) {

        QueryWrapper<EpsLmsCallButtonEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsLmsCallButtonEntity::getRouteCode, model.getRouteCode());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("已经存在相同的拉动代码，不允许重复！");
        }
    }

    @Override
    public void beforeUpdate(EpsLmsCallButtonEntity model) {
        QueryWrapper<EpsLmsCallButtonEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsLmsCallButtonEntity::getRouteCode, model.getRouteCode())
                .ne(EpsLmsCallButtonEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("已经存在相同的拉动代码，不允许重复！");
        }
    }


    /**
     * 获取所有的数据
     *
     * @return List<EpsLmsCallButtonEntity>
     */
    @Override
    public List<EpsLmsCallButtonEntity> getAllDatas() {
        Function<Object, ? extends List<EpsLmsCallButtonEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, 600);
    }

}