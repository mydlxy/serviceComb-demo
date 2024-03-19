package com.ca.mfd.prc.pmc.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pmc.entity.PmcStopCodeEntity;
import com.ca.mfd.prc.pmc.mapper.IPmcStopCodeMapper;
import com.ca.mfd.prc.pmc.service.IPmcStopCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 停线代码
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class PmcStopCodeServiceImpl extends AbstractCrudServiceImpl<IPmcStopCodeMapper, PmcStopCodeEntity> implements IPmcStopCodeService {

    private static final String cacheName = "PRC_PMC_STOP_CODE";
    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmcStopCodeEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmcStopCodeEntity model) {
        checkRepeat(model);
        removeCache();
    }

    private void checkRepeat(PmcStopCodeEntity model) {
        Long id = model.getId();
        QueryWrapper<PmcStopCodeEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmcStopCodeEntity::getStopCode, model.getStopCode()).ne(Objects.nonNull(id), PmcStopCodeEntity::getId, id)
                .eq(PmcStopCodeEntity::getIsDelete, false);
        Long count = selectCount(qry);
        if (count > 0) {
            throw new InkelinkException("停线代码不能重复");
        }
    }

    @Override
    public void afterUpdate(PmcStopCodeEntity model) {
        checkRepeat(model);
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmcStopCodeEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PmcStopCodeEntity> getAllDatas() {
        List<PmcStopCodeEntity> datas = localCache.getObject(cacheName);
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
}