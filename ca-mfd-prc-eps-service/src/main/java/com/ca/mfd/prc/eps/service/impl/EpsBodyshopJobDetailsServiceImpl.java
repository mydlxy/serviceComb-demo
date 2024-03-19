package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsBodyshopJobDetailsMapper;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.service.IEpsBodyshopJobDetailsService;
import com.ca.mfd.prc.eps.entity.EpsBodyshopJobDetailsEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 焊装车间执行码详情服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsBodyshopJobDetailsServiceImpl extends AbstractCrudServiceImpl<IEpsBodyshopJobDetailsMapper, EpsBodyshopJobDetailsEntity> implements IEpsBodyshopJobDetailsService {
    private static final String CACHE_NAME = "PRC_EPS_BODYSHOP_JOB_DETAILS";
    private static final Logger logger = LoggerFactory.getLogger(EpsBodyshopJobDetailsServiceImpl.class);
    @Autowired
    private LocalCache localCache;
    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsBodyshopJobDetailsEntity entity) {

        //checkUniqueness(entity,false);
        removeCache();
    }

    private void checkUniqueness(EpsBodyshopJobDetailsEntity model,Boolean isUpdate) {
        QueryWrapper<EpsBodyshopJobDetailsEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<EpsBodyshopJobDetailsEntity> qryLmp = qry.lambda();
        qryLmp.eq(EpsBodyshopJobDetailsEntity::getLineCode, model.getLineCode());
        if (isUpdate) {
            qryLmp.ne(EpsBodyshopJobDetailsEntity::getId, model.getId());
        }
        if (selectCount(qry) > 0) {
            throw new InkelinkException(String.format("区域%s已经存在", model.getLineCode()));
        }

    }

    @Override
    public void afterUpdate(EpsBodyshopJobDetailsEntity entity) {
        //checkUniqueness(entity, true);
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsBodyshopJobDetailsEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Wrapper<EpsBodyshopJobDetailsEntity> queryWrapper) {
        removeCache();
    }

    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    /**
     * 获取所有的数据
     *
     * @return
     */
    @Override
    public List<EpsBodyshopJobDetailsEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsBodyshopJobDetailsEntity>> getDataFunc = (obj) -> {
                List<EpsBodyshopJobDetailsEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsBodyshopJobDetailsEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

}