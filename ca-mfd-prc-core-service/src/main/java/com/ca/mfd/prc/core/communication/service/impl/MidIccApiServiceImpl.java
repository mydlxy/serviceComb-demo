package com.ca.mfd.prc.core.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.core.communication.dto.ApiResultVo;
import com.ca.mfd.prc.core.communication.dto.IccCategoryDto;
import com.ca.mfd.prc.core.communication.dto.IccDto;
import com.ca.mfd.prc.core.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.core.communication.entity.MidIccApiEntity;
import com.ca.mfd.prc.core.communication.entity.MidIccCategoryApiEntity;
import com.ca.mfd.prc.core.communication.mapper.IMidIccApiMapper;
import com.ca.mfd.prc.core.communication.service.IMidApiLogService;
import com.ca.mfd.prc.core.communication.service.IMidIccApiService;
import com.ca.mfd.prc.core.communication.service.IMidIccCategoryApiService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

    @Autowired
    private IMidApiLogService midApiLogService;

  /*  @Autowired
    private IMidIccApiService midIccApiService;*/

    @Autowired
    private IMidIccCategoryApiService midIccCategoryApiService;

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

}