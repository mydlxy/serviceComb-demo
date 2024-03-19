package com.ca.mfd.prc.core.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.communication.entity.MidIccCategoryApiEntity;
import com.ca.mfd.prc.core.communication.mapper.IMidIccCategoryApiMapper;
import com.ca.mfd.prc.core.communication.service.IMidIccCategoryApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 *
 * @Description: ICC分类接口中间表服务实现
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class MidIccCategoryApiServiceImpl extends AbstractCrudServiceImpl<IMidIccCategoryApiMapper, MidIccCategoryApiEntity> implements IMidIccCategoryApiService {
    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_ICC_CATEGORY_API";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidIccCategoryApiEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidIccCategoryApiEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidIccCategoryApiEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidIccCategoryApiEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidIccCategoryApiEntity> getAllDatas() {
        List<MidIccCategoryApiEntity> datas = localCache.getObject(cacheName);
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