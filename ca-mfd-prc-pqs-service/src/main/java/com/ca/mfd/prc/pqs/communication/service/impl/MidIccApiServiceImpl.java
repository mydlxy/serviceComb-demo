package com.ca.mfd.prc.pqs.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.communication.entity.MidIccApiEntity;
import com.ca.mfd.prc.pqs.communication.mapper.IMidIccApiMapper;
import com.ca.mfd.prc.pqs.communication.service.IMidIccApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 *
 * @Description: ICC接口中间表服务实现
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class MidIccApiServiceImpl extends AbstractCrudServiceImpl<IMidIccApiMapper, MidIccApiEntity> implements IMidIccApiService {

    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_ICC_API";


    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidIccApiEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidIccApiEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidIccApiEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidIccApiEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidIccApiEntity> getAllDatas() {
        List<MidIccApiEntity> datas = localCache.getObject(cacheName);
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


    @Override
    public List<MidIccApiEntity> getListByLog(Long id) {
        QueryWrapper<MidIccApiEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidIccApiEntity::getPrcMidApiLogId, id);
        return selectList(qry);
    }
}